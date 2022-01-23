package winsome.server_app.internal.tasks.impl;

import java.util.ArrayList;
import java.util.List;

import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.connection.socket_messages.client.LoginRequest;
import winsome.connection.socket_messages.server.LoginAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketClientTask;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public class LoginUserTask extends SocketClientTask
{
	private final LoginRequest message;

	public LoginUserTask(SocketTaskState socket, WinsomeData data, LoginRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void run(ServerThreadpool pool)
	{
		User requested_user = data.getUsers().get(message.username);
		
		if(checkLogin(requested_user))
		{
			sendLoginAnswer(requested_user, data);
		}
	}
	
	private boolean checkLogin(User requested_user)
	{		
		if(requested_user == null)
		{
			socket.sendAnswerMessage(new RequestExceptionAnswer("User does not exist."));
			return false;
		}

		if(!requested_user.login.checkPassword(message.password))
		{
			socket.sendAnswerMessage(new RequestExceptionAnswer("Incorrect password."));
			return false;
		}
		
		socket.setClientUser(message.username);
		return true;
	}
	
	private void sendLoginAnswer(User user, WinsomeData server_data)
	{			
		List<String> following = new ArrayList<String>();
		List<String> followers = new ArrayList<String>();
		List<Integer> posts = new ArrayList<Integer>();
		List<LoginAnswer.PostIdAndTitle> postsAndTitle = new ArrayList<LoginAnswer.PostIdAndTitle>();
		
		TaskUtils.lockUser(user, () ->
		{
			following.addAll(user.getFollowing());
			followers.addAll(user.getFollowers());
			posts.addAll(user.getPosts());
		});
		
		for(Integer postId : posts)
		{
			postsAndTitle.add( new LoginAnswer.PostIdAndTitle(
				postId,
				TaskUtils.getPostContent(postId, server_data).title
			));
		}
		
		LoginAnswer answer = new LoginAnswer(
			following.toArray(new String[0]),
			followers.toArray(new String[0]),
			postsAndTitle.toArray(new LoginAnswer.PostIdAndTitle[0]),
			WalletNotificationUpdater.getMulticastAddress()
		);

		socket.sendAnswerMessage(answer);	
	}
}

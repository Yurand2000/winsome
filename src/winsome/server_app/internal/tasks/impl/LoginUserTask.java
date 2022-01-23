package winsome.server_app.internal.tasks.impl;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

import winsome.connection.server_api.socket.SocketStateImpl;
import winsome.connection.socket_messages.client.LoginRequest;
import winsome.connection.socket_messages.server.LoginAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.user.User;

public class LoginUserTask implements WinsomeTask
{
	private final SelectionKey socket;
	private final LoginRequest message;

	public LoginUserTask(SelectionKey socket, LoginRequest message)
	{
		this.socket = socket;
		this.message = message;
	}

	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		User requested_user = server_data.users.get(message.username);
		
		if(checkLogin(requested_user))
		{
			sendLoginAnswer(requested_user, server_data);
		}
		
		TaskUtils.setSocketReadyToWrite(socket);
	}
	
	private boolean checkLogin(User requested_user)
	{
		SocketStateImpl infos = (SocketStateImpl) socket.attachment();
		
		if(requested_user == null)
		{
			TaskUtils.sendMessage(infos, new RequestExceptionAnswer("User does not exist."));
			return false;
		}

		if(!requested_user.login.checkPassword(message.password))
		{
			TaskUtils.sendMessage(infos, new RequestExceptionAnswer("Incorrect password."));
			return false;
		}
		
		infos.setSocketUser(message.username);
		return true;
	}
	
	private void sendLoginAnswer(User user, WinsomeData server_data)
	{			
		List<String> following = new ArrayList<String>();
		List<String> followers = new ArrayList<String>();
		List<Integer> posts = new ArrayList<Integer>();
		List<LoginAnswer.PostIdAndTitle> postsAndTitle = new ArrayList<LoginAnswer.PostIdAndTitle>();
		
		WinsomeData.lockUser(user, () ->
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
			"upd_multicast_address missing" //!
		);

		TaskUtils.sendMessage((SocketStateImpl) socket.attachment(), answer);	
	}
}
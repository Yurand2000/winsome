package winsome.server_app.internal.tasks.impl;

import java.util.ArrayList;
import java.util.List;

import winsome.connection.socket_messages.client.LoginRequest;
import winsome.connection.socket_messages.server.LoginAnswer;
import winsome.server_app.internal.WinsomeData;
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
	public void execute(ServerThreadpool pool)
	{
		User requested_user = data.getUsers().get(message.username);
		
		checkLogin(requested_user);
		sendLoginAnswer(requested_user, data);
	}
	
	private void checkLogin(User requested_user)
	{		
		if(requested_user == null)
		{
			throw new RuntimeException("User does not exist.");
		}

		if(!requested_user.login.checkPassword(message.password))
		{
			throw new RuntimeException("Incorrect password.");
		}
		
		socket.setClientUser(message.username);
	}
	
	private void sendLoginAnswer(User user, WinsomeData server_data)
	{			
		List<String> following = new ArrayList<String>();
		List<String> followers = new ArrayList<String>();
		List<Integer> posts = new ArrayList<Integer>();
		
		TaskUtils.lockUser(user, () ->
		{
			following.addAll(user.getFollowing());
			followers.addAll(user.getFollowers());
			posts.addAll(user.getPosts());
		});
		
		LoginAnswer answer = new LoginAnswer(
			followers.toArray(new String[0]),
			following.toArray(new String[0]),
			server_data.getWalletUpdater().getMulticastAddress()
		);

		socket.sendAnswerMessage(answer);	
	}
}

package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.FollowUserRequest;
import winsome.connection.socket_messages.server.FollowUserAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public class FollowUserTask extends LoggedUserTask
{
	private final FollowUserRequest message;

	public FollowUserTask(SocketTaskState socket, WinsomeData data, FollowUserRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		checkUserNotAlreadyFollowed(user);
		setUserToFollowAndNotify(user);
		socket.sendAnswerMessage(new FollowUserAnswer());
	}
	
	private void checkUserNotAlreadyFollowed(User user)
	{
		if(user.username == message.username)
		{
			throw new RuntimeException("Cannot follow yourself.");
		}
		
		if(user.getFollowing().contains(message.username))
		{
			throw new RuntimeException("User already followed.");
		}
	}
	
	private void setUserToFollowAndNotify(User user)
	{
		User followed = TaskUtils.getUser(message.username, data);
		
		TaskUtils.lockTwoUsers(user, followed, () ->
		{
			user.addFollowing(followed.username);
			followed.addFollower(user.username);
			data.getFollowerUpdater().notifyNewFollower(followed.username, user.username);
		});
	}
}

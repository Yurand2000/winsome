package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.UnfollowUserRequest;
import winsome.connection.socket_messages.server.UnfollowUserAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public class UnfollowUserTask extends LoggedUserTask
{
	private final UnfollowUserRequest message;

	public UnfollowUserTask(SocketTaskState socket, WinsomeData data, UnfollowUserRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		checkUserIsFollowed(user);
		setUserToUnfollowAndNotify(user);
		socket.sendAnswerMessage(new UnfollowUserAnswer());
	}
	
	private void checkUserIsFollowed(User user)
	{
		if(!user.getFollowing().contains(message.username))
		{
			throw new RuntimeException("User already followed.");
		}
	}
	
	private void setUserToUnfollowAndNotify(User user)
	{
		User followed = TaskUtils.getUser(message.username, data);
		
		TaskUtils.lockTwoUsers(user, followed, () ->
		{
			user.removeFollowing(followed.username);
			followed.removeFollower(user.username);
			data.getFollowerUpdater().notifyRemovedFollower(followed.username, user.username);
		});
	}
}

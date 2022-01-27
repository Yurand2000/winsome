package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.client_app.api.exceptions.AlreadyFollowingUserException;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.FollowUserRequest;
import winsome.connection.socket_messages.server.FollowUserAnswer;

public class FollowUserExecutor extends DefaultTaskExecutor
{
	private final String user_to_follow;
	
	public FollowUserExecutor(String user_to_follow)
	{
		this.user_to_follow = user_to_follow;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		checkNotAlreadyFollowingUser(api);
		
		FollowUserRequest request = new FollowUserRequest(user_to_follow);
		connection.sendMessage(request);
		connection.readMessage(FollowUserAnswer.class);
		
		addUserToFollowingList(api);
	}

	private void checkNotAlreadyFollowingUser(ApplicationLoggedAPI api)
	{
		if(api.getFollowing().contains(user_to_follow))
		{
			throw new AlreadyFollowingUserException(user_to_follow);
		}
	}
	
	private void addUserToFollowingList(ApplicationLoggedAPI api)
	{
		api.getFollowing().add(user_to_follow);
	}
}

package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.client_app.api.exceptions.NotFollowingUserException;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.UnfollowUserRequest;
import winsome.connection.socket_messages.server.UnfollowUserAnswer;

public class UnfollowUserExecutor extends DefaultTaskExecutor
{
	private final String user_to_unfollow;
	
	public UnfollowUserExecutor(String user_to_unfollow)
	{
		this.user_to_unfollow = user_to_unfollow;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		checkNotYetFollowingUser(api);
		
		UnfollowUserRequest request = new UnfollowUserRequest(user_to_unfollow);
		connection.sendMessage(request);		
		connection.readMessage(UnfollowUserAnswer.class);
		
		removeUserFromFollowingList(api);
	}

	private void checkNotYetFollowingUser(ApplicationLoggedAPI api)
	{
		if(!api.getFollowing().contains(user_to_unfollow))
		{
			throw new NotFollowingUserException(user_to_unfollow);
		}
	}
	
	private void removeUserFromFollowingList(ApplicationLoggedAPI api)
	{
		api.getFollowing().remove(user_to_unfollow);
	}
}

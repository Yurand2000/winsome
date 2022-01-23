package winsome.connection.client_api.socket.tasks;

import java.io.IOException;

import winsome.client_app.api.exceptions.PostOwnedException;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.RatePostRequest;
import winsome.connection.socket_messages.server.RatePostAnswer;

public class RatePostExecutor extends DefaultTaskExecutor
{
	private final Integer postId;
	private final Boolean liked;
	
	public RatePostExecutor(Integer postId, boolean liked)
	{
		this.postId = postId;
		this.liked = liked;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		checkIsNotPostAuthor(api);
		
		RatePostRequest request = new RatePostRequest(postId, liked);
		connection.sendMessage(request);
		
		@SuppressWarnings("unused")
		RatePostAnswer answer = connection.readMessage(RatePostAnswer.class);
	}
	
	private void checkIsNotPostAuthor(ApplicationLoggedAPI api)
	{
		if(api.getBlog().containsKey(postId))
		{
			throw new PostOwnedException(postId);
		}
	}
}

package winsome.client_app.internal.tasks;

import java.io.IOException;

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
		RatePostRequest request = new RatePostRequest(postId, liked);
		connection.sendMessage(request);
		
		@SuppressWarnings("unused")
		RatePostAnswer answer = connection.readMessage(RatePostAnswer.class);
	}
}

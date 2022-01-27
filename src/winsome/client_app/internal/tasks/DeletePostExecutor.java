package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.DeletePostRequest;
import winsome.connection.socket_messages.server.DeletePostAnswer;

public class DeletePostExecutor extends DefaultTaskExecutor
{
	private final Integer postId;
	
	public DeletePostExecutor(Integer postId)
	{
		this.postId = postId;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{		
		DeletePostRequest request = new DeletePostRequest(postId);
		connection.sendMessage(request);
		connection.readMessage(DeletePostAnswer.class);
	}
}

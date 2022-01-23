package winsome.connection.client_api.socket.tasks;

import java.io.IOException;

import winsome.client_app.api.exceptions.PostNotOwnedException;
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
		checkCanDeletePost(api);
		
		DeletePostRequest request = new DeletePostRequest(postId);
		connection.sendMessage(request);
		
		@SuppressWarnings("unused")
		DeletePostAnswer answer = connection.readMessage(DeletePostAnswer.class);
		
		deletePost(api);
	}
	
	private void checkCanDeletePost(ApplicationLoggedAPI api)
	{
		if(!api.getBlog().containsKey(postId))
		{
			throw new PostNotOwnedException(postId);
		}
	}
	
	private void deletePost(ApplicationLoggedAPI api)
	{
		api.getBlog().remove(postId);
	}
}

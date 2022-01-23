package winsome.connection.client_api.socket.tasks;

import java.io.IOException;

import winsome.client_app.api.exceptions.PostOwnedException;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.CommentPostRequest;
import winsome.connection.socket_messages.server.CommentPostAnswer;

public class CommentPostExecutor extends DefaultTaskExecutor
{
	private final Integer postId;
	private final String comment;
	
	public CommentPostExecutor(Integer postId, String comment)
	{
		this.postId = postId;
		this.comment = comment;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		checkIsNotPostAuthor(api);
		
		CommentPostRequest request = new CommentPostRequest(postId, comment);
		connection.sendMessage(request);
		
		@SuppressWarnings("unused")
		CommentPostAnswer answer = connection.readMessage(CommentPostAnswer.class);
	}
	
	private void checkIsNotPostAuthor(ApplicationLoggedAPI api)
	{
		if(api.getBlog().containsKey(postId))
		{
			throw new PostOwnedException(postId);
		}
	}
}

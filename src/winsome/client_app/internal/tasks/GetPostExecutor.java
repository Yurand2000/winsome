package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.client_app.api.Post;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.GetPostRequest;
import winsome.connection.socket_messages.server.GetPostAnswer;

public class GetPostExecutor extends DefaultTaskExecutor
{
	private final Integer postId;
	private Post retrived_post;
	
	public GetPostExecutor(Integer postId)
	{
		this.postId = postId;
		this.retrived_post = null;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{		
		GetPostRequest request = new GetPostRequest(postId);
		connection.sendMessage(request);
		
		GetPostAnswer answer = connection.readMessage(GetPostAnswer.class);
		retrived_post = answer.post;
	}
	
	public Post getRetrivedPost()
	{
		return retrived_post;
	}
}

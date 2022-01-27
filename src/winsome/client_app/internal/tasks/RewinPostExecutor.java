package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.RewinPostRequest;
import winsome.connection.socket_messages.server.RewinPostAnswer;

public class RewinPostExecutor extends DefaultTaskExecutor
{
	private final Integer postId;
	private Integer newPostId;
	
	public RewinPostExecutor(Integer postId)
	{
		this.postId = postId;
		this.newPostId = null;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{		
		RewinPostRequest request = new RewinPostRequest(postId);
		connection.sendMessage(request);
		
		RewinPostAnswer answer = connection.readMessage(RewinPostAnswer.class);		
		newPostId = answer.newPostId;
	}
	
	public Integer getNewPostId()
	{
		return newPostId;
	}
}

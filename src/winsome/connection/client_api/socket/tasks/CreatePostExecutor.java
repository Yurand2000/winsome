package winsome.connection.client_api.socket.tasks;

import java.io.IOException;

import winsome.client_app.api.PostShort;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.CreatePostRequest;
import winsome.connection.socket_messages.server.CreatePostAnswer;

public class CreatePostExecutor extends DefaultTaskExecutor
{
	private final String title;
	private final String content;
	private Integer newPostId;
	
	public CreatePostExecutor(String title, String content)
	{
		this.title = title;
		this.content = content;
		this.newPostId = null;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		CreatePostRequest request = new CreatePostRequest(title, content);
		connection.sendMessage(request);
		
		CreatePostAnswer answer = connection.readMessage(CreatePostAnswer.class);
		
		newPostId = answer.newPostId;
		api.getBlog().put(answer.newPostId, makeNewPost(answer.newPostId, api));
	}
	
	private PostShort makeNewPost(Integer postId, ApplicationLoggedAPI api)
	{
		return new PostShort(postId, api.getThisUser(), title);
	}
	
	public Integer getNewPostId()
	{
		return newPostId;
	}
}

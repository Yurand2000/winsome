package winsome.client_app.internal.tasks;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import winsome.client_app.api.PostShort;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.GetBlogRequest;
import winsome.connection.socket_messages.server.GetBlogAnswer;

public class GetBlogExecutor extends DefaultTaskExecutor
{
	private final List<PostShort> blog;
	
	public GetBlogExecutor(List<PostShort> blog)
	{
		this.blog = blog;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		GetBlogRequest request = new GetBlogRequest();
		connection.sendMessage(request);
		
		GetBlogAnswer answer = connection.readMessage(GetBlogAnswer.class);
		copyAnswerToBlogList(answer.blog);
	}
	
	private void copyAnswerToBlogList(PostShort[] answer_blog)
	{
		blog.addAll( Arrays.asList(answer_blog) );
	}
}

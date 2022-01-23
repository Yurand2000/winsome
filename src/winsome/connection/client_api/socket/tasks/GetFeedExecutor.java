package winsome.connection.client_api.socket.tasks;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import winsome.client_app.api.PostShort;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.GetFeedRequest;
import winsome.connection.socket_messages.server.GetFeedAnswer;

public class GetFeedExecutor extends DefaultTaskExecutor
{
	private final List<PostShort> feed;
	
	public GetFeedExecutor(List<PostShort> feed)
	{
		this.feed = feed;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		GetFeedRequest request = new GetFeedRequest();
		connection.sendMessage(request);
		
		GetFeedAnswer answer = connection.readMessage(GetFeedAnswer.class);
		copyAnswerToFeedList(answer.feed);
	}
	
	private void copyAnswerToFeedList(PostShort[] answer_feed)
	{
		feed.addAll( Arrays.asList(answer_feed) );
	}
}

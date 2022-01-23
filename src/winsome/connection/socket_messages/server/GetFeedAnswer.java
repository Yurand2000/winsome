package winsome.connection.socket_messages.server;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.client_app.api.PostShort;
import winsome.connection.socket_messages.Message;

@JsonTypeName("get_feed_answer")
public class GetFeedAnswer extends Message
{
	public final PostShort[] feed;
	
	@SuppressWarnings("unused")
	private GetFeedAnswer() { feed = null; }
	
	public GetFeedAnswer(List<PostShort> feed)
	{
		this.feed = feed.toArray(new PostShort[0]);
	}
	
	public GetFeedAnswer(PostShort[] feed)
	{
		this.feed = feed;
	}
}

package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("rate_post_request")
public class RatePostRequest extends Message
{
	public final Integer postId;
	public final Boolean liked;

	@SuppressWarnings("unused")
	private RatePostRequest() { postId = null; liked = null; }
	
	public RatePostRequest(Integer postId, Boolean liked)
	{
		this.postId = postId;
		this.liked = liked;
	}
}

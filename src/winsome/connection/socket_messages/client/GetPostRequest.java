package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("get_post_request")
public class GetPostRequest extends Message
{
	public final Integer postId;

	@SuppressWarnings("unused")
	private GetPostRequest() { postId = null; }
	
	public GetPostRequest(Integer postId)
	{
		this.postId = postId;
	}
}

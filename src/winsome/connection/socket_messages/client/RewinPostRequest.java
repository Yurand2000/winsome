package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("rewin_post_request")
public class RewinPostRequest extends Message
{
	public final Integer postId;

	@SuppressWarnings("unused")
	private RewinPostRequest() { postId = null; }
	
	public RewinPostRequest(Integer postId)
	{
		this.postId = postId;
	}
}

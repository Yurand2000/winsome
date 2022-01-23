package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("delete_post_request")
public class DeletePostRequest extends Message
{
	public final Integer postId;

	@SuppressWarnings("unused")
	private DeletePostRequest() { postId = null; }
	
	public DeletePostRequest(Integer postId)
	{
		this.postId = postId;
	}
}

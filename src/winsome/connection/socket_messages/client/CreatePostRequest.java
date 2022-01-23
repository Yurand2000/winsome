package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("create_post_request")
public class CreatePostRequest extends Message
{
	public final String title;
	public final String content;
	
	@SuppressWarnings("unused")
	private CreatePostRequest() { title = null; content = null; }
	
	public CreatePostRequest(String title, String content)
	{
		this.title = title;
		this.content = content;
	}
}

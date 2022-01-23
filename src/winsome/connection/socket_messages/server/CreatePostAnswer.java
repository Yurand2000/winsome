package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("create_post_answer")
public class CreatePostAnswer extends Message
{
	public final Integer newPostId;
	
	@SuppressWarnings("unused")
	private CreatePostAnswer() { newPostId = null; }
	
	public CreatePostAnswer(Integer newPostId)
	{
		this.newPostId = newPostId;
	}
}

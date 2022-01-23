package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("rewin_post_answer")
public class RewinPostAnswer extends Message
{
	public final Integer newPostId;
	
	@SuppressWarnings("unused")
	private RewinPostAnswer() { newPostId = null; }
	
	public RewinPostAnswer(Integer newPostId)
	{
		this.newPostId = newPostId;
	}
}

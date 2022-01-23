package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("comment_post_request")
public class CommentPostRequest extends Message
{
	public final Integer postId;
	public final String comment;

	@SuppressWarnings("unused")
	private CommentPostRequest() { postId = null; comment = null; }
	
	public CommentPostRequest(Integer postId, String comment)
	{
		this.postId = postId;
		this.comment = comment;
	}
}

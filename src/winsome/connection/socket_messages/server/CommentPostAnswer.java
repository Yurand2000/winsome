package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("comment_post_answer")
@JsonSerialize
public class CommentPostAnswer extends Message
{
	public CommentPostAnswer() { }
}

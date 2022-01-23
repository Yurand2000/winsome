package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("delete_post_answer")
@JsonSerialize
public class DeletePostAnswer extends Message
{
	public DeletePostAnswer() { }
}

package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("follow_user_answer")
@JsonSerialize
public class FollowUserAnswer extends Message
{
	public FollowUserAnswer() { }
}

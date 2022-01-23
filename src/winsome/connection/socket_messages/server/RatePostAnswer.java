package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("rate_post_answer")
@JsonSerialize
public class RatePostAnswer extends Message
{
	public RatePostAnswer() { }
}

package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("logout_answer")
@JsonSerialize
public class LogoutAnswer extends Message
{
	public LogoutAnswer() { }
}

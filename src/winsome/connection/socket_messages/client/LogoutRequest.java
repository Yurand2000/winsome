package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("logout_request")
@JsonSerialize
public class LogoutRequest extends Message
{
	public LogoutRequest() { }
}

package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("list_user_request")
@JsonSerialize
public class ListUserRequest extends Message
{
	public ListUserRequest() { }
}

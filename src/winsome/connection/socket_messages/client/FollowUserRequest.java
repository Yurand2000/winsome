package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("follow_user_request")
public class FollowUserRequest extends Message
{
	public final String username;
	
	@SuppressWarnings("unused")
	private FollowUserRequest() { username = null;}
	
	public FollowUserRequest(String username)
	{
		this.username = username;
	}
}

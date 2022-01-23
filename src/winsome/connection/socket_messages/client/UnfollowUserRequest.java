package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("unfollow_user_request")
public class UnfollowUserRequest extends Message
{
	public final String username;
	
	@SuppressWarnings("unused")
	private UnfollowUserRequest() { username = null;}
	
	public UnfollowUserRequest(String username)
	{
		this.username = username;
	}
}

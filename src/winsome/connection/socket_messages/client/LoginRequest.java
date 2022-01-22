package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("login_request")
public class LoginRequest extends Message
{
	public final String username;
	public final String password;

	@SuppressWarnings("unused")
	private LoginRequest() { username = null; password = null; }
	
	public LoginRequest(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
}

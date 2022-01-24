package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("login_answer")
public class LoginAnswer extends Message
{
	public final String[] followed_by_users;
	public final String[] following_users;
	public final String udp_multicast_address;
	
	@SuppressWarnings("unused")
	private LoginAnswer() { followed_by_users = null; following_users = null; udp_multicast_address = null; }
	
	public LoginAnswer(String[] followed_by_users, String[] following_users, String udp_multicast_address)
	{
		this.followed_by_users = followed_by_users;
		this.following_users = following_users;
		this.udp_multicast_address = udp_multicast_address;
	}
}

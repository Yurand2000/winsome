package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("login_answer")
public class LoginAnswer extends Message
{
	public final String[] followed_by_users;
	public final String[] following_users;
	public final PostIdAndTitle[] my_blog;
	public final String udp_multicast_address;
	
	public LoginAnswer(String[] followed_by_users, String[] following_users, PostIdAndTitle[] my_blog, String udp_multicast_address)
	{
		this.followed_by_users = followed_by_users;
		this.following_users = following_users;
		this.my_blog = my_blog;
		this.udp_multicast_address = udp_multicast_address;
	}
	
	public static class PostIdAndTitle
	{
		public final Integer postId;
		public final String title;
		
		public PostIdAndTitle(Integer postId, String title)
		{
			this.postId = postId;
			this.title = title;
		}
	}
}

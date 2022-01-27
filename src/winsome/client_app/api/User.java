package winsome.client_app.api;

/**
 * The user class, containing information retrived about other users.
 * Currently only displays the username.
 */
public class User
{
	public final String username;
	
	public User(String username)
	{
		this.username = username;
	}
	
	@Override
	public boolean equals(Object u)
	{
		if(u.getClass() == User.class)
		{
			return username == ((User)u).username;
		}
		else
		{
			return false;
		}
	}
}

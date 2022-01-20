package winsome.server_app.user.exceptions;

public class FollowerAlreadyAddedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public FollowerAlreadyAddedException(String username)
	{
		super("The given follower \'" + username + "\' was already added to the following list.");
	}
}

package winsome.server_app.user.exceptions;

public class UserWasNotFollowedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public UserWasNotFollowedException(String username)
	{
		super("The given user \'" + username + "\' was not present in the following list.");
	}
}
package winsome.client.api.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyTakenException()
	{
		super("The requested username is already taken. Registration failed.");
	}
}

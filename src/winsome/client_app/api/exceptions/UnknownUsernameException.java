package winsome.client_app.api.exceptions;

public class UnknownUsernameException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UnknownUsernameException()
	{
		super("Cannot execute: username is not known.");
	}
}
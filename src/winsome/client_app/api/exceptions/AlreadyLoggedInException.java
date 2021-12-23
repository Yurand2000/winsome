package winsome.client_app.api.exceptions;

public class AlreadyLoggedInException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public AlreadyLoggedInException()
	{
		super("Cannot execute: you are already logged in.");
	}
}
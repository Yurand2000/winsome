package winsome.client_app.api.exceptions;

public class NotLoggedInException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public NotLoggedInException()
	{
		super("Cannot execute: not logged in.");
	}
}
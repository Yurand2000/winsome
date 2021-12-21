package winsome.client.api.exceptions;

public class IncorrectPasswordException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException()
	{
		super("Cannot execute: given password is incorrect.");
	}
}
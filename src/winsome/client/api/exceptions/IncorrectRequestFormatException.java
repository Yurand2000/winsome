package winsome.client.api.exceptions;

public class IncorrectRequestFormatException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public IncorrectRequestFormatException()
	{
		super("Given request parameters format is invalid.");
	}
}

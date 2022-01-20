package winsome.client_app.api.exceptions;

public class TooManyTagsException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public TooManyTagsException()
	{
		super("Too many tags specified for registration");
	}
}

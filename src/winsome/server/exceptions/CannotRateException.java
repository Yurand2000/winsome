package winsome.server.exceptions;

public class CannotRateException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public CannotRateException()
	{
		super("The user can't rate this post because they have already rated it.");
	}
}
package winsome.client_app.api.exceptions;

public class ServerInternalException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ServerInternalException(String error)
	{
		super("Internal server exception: " + error);
	}
}

package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class ServerInternalException extends APIException
{
	private static final long serialVersionUID = 1L;

	public ServerInternalException(String error)
	{
		super("Internal server exception: " + error);
	}
}

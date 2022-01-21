package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class CouldNotConnectException extends APIException
{
	private static final long serialVersionUID = 1L;

	public CouldNotConnectException()
	{
		super("Could not connect to remote server.");
	}
}

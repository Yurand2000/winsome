package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class UnknownUsernameException extends APIException
{
	private static final long serialVersionUID = 1L;

	public UnknownUsernameException()
	{
		super("Cannot execute: username is not known.");
	}
}
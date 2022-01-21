package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class AlreadyLoggedInException extends APIException
{
	private static final long serialVersionUID = 1L;

	public AlreadyLoggedInException()
	{
		super("Cannot execute: you are already logged in.");
	}
}
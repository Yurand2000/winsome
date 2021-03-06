package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class NotLoggedInException extends APIException
{
	private static final long serialVersionUID = 1L;

	public NotLoggedInException()
	{
		super("Cannot execute: not logged in.");
	}
}
package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class IncorrectPasswordException extends APIException
{
	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException()
	{
		super("Cannot execute: given password is incorrect.");
	}
}
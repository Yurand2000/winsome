package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class UsernameAlreadyTakenException extends APIException
{
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyTakenException()
	{
		super("The requested username is already taken. Registration failed.");
	}
}

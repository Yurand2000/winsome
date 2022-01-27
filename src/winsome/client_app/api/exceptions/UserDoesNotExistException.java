package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class UserDoesNotExistException extends APIException
{
	private static final long serialVersionUID = 1L;

	public UserDoesNotExistException(String username)
	{
		super("Given user with username " + username + " does not exist.");
	}
}

package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class AlreadyFollowingUserException extends APIException
{
	private static final long serialVersionUID = 1L;

	public AlreadyFollowingUserException(String user)
	{
		super("Error: you are already following the user: " + user);
	}
}

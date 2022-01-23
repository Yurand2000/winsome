package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class NotFollowingUserException extends APIException
{
	private static final long serialVersionUID = 1L;

	public NotFollowingUserException(String user)
	{
		super("Error: you are not following the user: " + user);
	}
}

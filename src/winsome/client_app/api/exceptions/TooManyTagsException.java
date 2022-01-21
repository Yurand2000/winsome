package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class TooManyTagsException extends APIException
{
	private static final long serialVersionUID = 1L;

	public TooManyTagsException()
	{
		super("Too many tags specified for registration");
	}
}

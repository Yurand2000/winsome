package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class IncorrectRequestFormatException extends APIException
{
	private static final long serialVersionUID = 1L;

	public IncorrectRequestFormatException()
	{
		super("Given request parameters format is invalid.");
	}
}

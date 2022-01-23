package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class IncorrectFormatException extends APIException
{
	private static final long serialVersionUID = 1L;

	public IncorrectFormatException(String message)
	{
		super(message);
	}
}

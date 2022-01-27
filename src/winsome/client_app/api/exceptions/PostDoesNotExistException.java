package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class PostDoesNotExistException extends APIException
{
	private static final long serialVersionUID = 1L;

	public PostDoesNotExistException(Integer postId)
	{
		super("Given post with id " + postId.toString() + " does not exist.");
	}
}

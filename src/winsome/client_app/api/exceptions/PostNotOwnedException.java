package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class PostNotOwnedException extends APIException
{
	private static final long serialVersionUID = 1L;

	public PostNotOwnedException(Integer postId)
	{
		super("Cannot execute: you do not own the post with id: " + postId);
	}
}
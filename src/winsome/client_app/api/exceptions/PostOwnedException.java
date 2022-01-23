package winsome.client_app.api.exceptions;

import winsome.client_app.api.APIException;

public class PostOwnedException extends APIException
{
	private static final long serialVersionUID = 1L;

	public PostOwnedException(Integer postId)
	{
		super("Cannot execute: you do own the post with id: " + postId);
	}
}
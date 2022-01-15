package winsome.server.test.genericPost;

import static org.junit.jupiter.api.Assertions.*;
import winsome.server.PostLikes;

public class PostLikesTestImpl implements PostLikes
{
	private String expected_user;
	private boolean can_rate_called = false;
	private boolean like_called = false;
	private boolean dislike_called = false;
	private boolean already_rated = false;
	private int likes = 0;

	@Override
	public void addLike(String username)
	{
		assertEquals(username, expected_user);
		assertTrue(can_rate_called);
		likes++;
		can_rate_called = false;
		like_called = true;
	}

	@Override
	public void addDislike(String username)
	{
		assertEquals(username, expected_user);
		assertTrue(can_rate_called);
		likes--;
		can_rate_called = false;
		dislike_called = true;
	}

	@Override
	public int getLikes()
	{
		return likes;
	}

	@Override
	public int getDislikes()
	{
		return -likes;
	}

	@Override
	public boolean canRate(String username)
	{
		assertEquals(username, expected_user);
		can_rate_called = true;
		return !already_rated;
	}

	public void checkLikeCalled()
	{
		assertTrue(like_called);
		like_called = false;
	}
	
	public void checkDislikeCalled()
	{
		assertTrue(dislike_called);
		dislike_called = false;
	}
	
	public void setExpectedUser(String username)
	{
		expected_user = username;
	}
	
	public void setAlreadyRated(boolean already_rated)
	{
		this.already_rated = already_rated;
	}
}

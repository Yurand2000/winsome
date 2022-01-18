package winsome.server.post.test.genericPost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import winsome.server.post.RewardState;

public class RewardStateTestImpl implements RewardState
{
	private String expected_user;
	private boolean like_called = false;
	private boolean dislike_called = false;
	private boolean add_comment_called = false;
	
	@Override
	public void addLike(String username)
	{
		assertEquals(username, expected_user);
		like_called = true;
	}

	@Override
	public void addDislike()
	{
		dislike_called = true;
	}

	@Override
	public void addComment(String username)
	{
		assertEquals(username, expected_user);
		add_comment_called = true;
	}

	@Override
	public Reward calcLastReward() {
		return null;
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
	
	public void checkAddCommentCalled()
	{
		assertTrue(add_comment_called);
		add_comment_called = false;
	}
	
	public void setExpectedUser(String username)
	{
		expected_user = username;
	}

}

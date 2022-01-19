package winsome.server.post.test.genericPost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.server.post.RewardState;

@JsonSerialize
@JsonTypeName("reward_state_test_impl")
public class RewardStateTestImpl implements RewardState
{
	@JsonIgnore() private String expected_user;
	@JsonIgnore() private boolean like_called = false;
	@JsonIgnore() private boolean dislike_called = false;
	@JsonIgnore() private boolean add_comment_called = false;
	@JsonIgnore() private boolean clone_called = false;
	
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

	public RewardStateTestImpl clone()
	{
		clone_called = true;
		return new RewardStateTestImpl();
	}
	
	@JsonIgnore() public boolean getCloneCalled()
	{
		return clone_called;
	}
}

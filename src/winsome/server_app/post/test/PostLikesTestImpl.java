package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import winsome.server_app.post.PostLikes;

@JsonTypeName("post_likes_test_impl")
class PostLikesTestImpl implements PostLikes
{
	@JsonIgnore() private String expected_user;
	@JsonIgnore() private boolean like_called = false;
	@JsonIgnore() private boolean dislike_called = false;
	@JsonIgnore() private boolean clone_called = false;
	@JsonProperty() private int likes = 0;
	@JsonProperty() private int dislikes = 0;

	@Override
	public void addLike(String username)
	{
		assertEquals(username, expected_user);
		likes++;
		like_called = true;
	}

	@Override
	public void addDislike(String username)
	{
		assertEquals(username, expected_user);
		dislikes++;
		dislike_called = true;
	}

	@Override
	@JsonIgnore() public int getLikes()
	{
		return likes;
	}

	@Override
	@JsonIgnore() public int getDislikes()
	{
		return dislikes;
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
	
	public PostLikesTestImpl clone()
	{
		clone_called = true;
		return new PostLikesTestImpl();
	}
	
	@JsonIgnore() public boolean getCloneCalled()
	{
		return clone_called;
	}

	@Override
	public boolean canRate(String username)
	{
		return false;
	}
}

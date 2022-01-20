package winsome.server_app.post.test.genericPost;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.server_app.post.PostComments;

@JsonSerialize
@JsonTypeName("post_comments_test_impl")
public class PostCommentsTestImpl implements PostComments
{
	@JsonIgnore() private Comment expected_commment;
	@JsonIgnore() private boolean add_comment_called = false;
	@JsonIgnore() private boolean clone_called = false;
	
	@Override
	public void addComment(String username, String comment)
	{
		assertEquals(expected_commment.username, username);
		assertEquals(expected_commment.comment, comment);
		add_comment_called = true;
	}

	@Override
	@JsonIgnore() public List<Comment> getComments()
	{
		return null;
	}

	public void setExpectedComment(String username, String comment)
	{
		expected_commment = new Comment(username, comment);
	}
	
	public void checkAddCommentCalled()
	{
		assertTrue(add_comment_called);
		add_comment_called = false;
	}
	
	public PostCommentsTestImpl clone()
	{
		clone_called = true;
		return new PostCommentsTestImpl();
	}
	
	@JsonIgnore() public boolean getCloneCalled()
	{
		return clone_called;
	}
}

package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.server_app.post.PostComments;

@JsonTypeName("post_comments_test_impl")
class PostCommentsTestImpl implements PostComments
{
	@JsonProperty() private List<Comment> comments = new ArrayList<Comment>();
	@JsonIgnore() private Comment expected_comment;
	@JsonIgnore() private boolean add_comment_called = false;
	@JsonIgnore() private boolean clone_called = false;
	
	@Override
	public void addComment(String username, String comment)
	{
		assertEquals(expected_comment.username, username);
		assertEquals(expected_comment.comment, comment);
		comments.add(expected_comment);
		add_comment_called = true;
	}

	@Override
	@JsonIgnore() public List<Comment> getComments()
	{
		return comments;
	}

	public void setExpectedComment(String username, String comment)
	{
		expected_comment = new Comment(username, comment);
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

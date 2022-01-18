package winsome.server.post.test.genericPost;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import winsome.server.post.PostComments;

public class PostCommentsTestImpl implements PostComments
{
	private Comment expected_commment;
	private boolean add_comment_called = false;
	
	@Override
	public void addComment(String username, String comment)
	{
		assertEquals(expected_commment.username, username);
		assertEquals(expected_commment.comment, comment);
		add_comment_called = true;
	}

	@Override
	public List<Comment> getComments()
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
}

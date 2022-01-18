package winsome.server.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import winsome.server.post.PostComments;
import winsome.server.post.PostCommentsImpl;

class PostCommentsImplTest
{

	@Test
	void constructors()
	{
		PostCommentsImpl default_constructed = new PostCommentsImpl();
		assertTrue(default_constructed.getComments().isEmpty());

		PostComments.Comment[] comments = new PostComments.Comment[]
		{
			new PostComments.Comment("Giulio", "Commento1"),
			new PostComments.Comment("Nicola", "Commento2"),
			new PostComments.Comment("Giulio", "Commento3")
		};
		PostCommentsImpl param_constructed = new PostCommentsImpl(Arrays.asList(comments));
		assertEquals(param_constructed.getComments().size(), 3);
	}

	void testAddComment()
	{
		PostCommentsImpl comments = new PostCommentsImpl();

		assertEquals(comments.getComments().size(), 0);
		comments.addComment("Giulio", "Commento1");
		assertEquals(comments.getComments().size(), 1);
		comments.addComment("Giulio", "Commento2");
		assertEquals(comments.getComments().size(), 2);
	}
	
	void testGetCommentsReturnsUnmodifiableList()
	{
		PostCommentsImpl comments = new PostCommentsImpl();
		assertDoesNotThrow(() -> { comments.getComments().isEmpty(); });
		assertThrows(UnsupportedOperationException.class,
			() -> { comments.getComments().add(null); });
	}
}

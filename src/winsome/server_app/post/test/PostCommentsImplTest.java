package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.post.PostComments;
import winsome.server_app.post.PostCommentsImpl;

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

	@Test
	void testAddComment()
	{
		PostCommentsImpl comments = new PostCommentsImpl();

		assertEquals(comments.getComments().size(), 0);
		comments.addComment("Giulio", "Commento1");
		assertEquals(comments.getComments().size(), 1);
		comments.addComment("Giulio", "Commento2");
		assertEquals(comments.getComments().size(), 2);
	}

	@Test
	void testGetComments()
	{
		PostCommentsImpl comments = new PostCommentsImpl();
		comments.addComment("Giulio", "Commento1");
		assertEquals(comments.getComments().size(), 1);
		assertEquals(comments.getComments().get(0).username, "Giulio");
		assertEquals(comments.getComments().get(0).comment, "Commento1");
	}
	
	@Test
	void checkClone()
	{
		PostCommentsImpl comments = new PostCommentsImpl();
		comments.addComment("Giulio", "Commento1");
		comments.addComment("Luigi", "Commento2");
		
		PostCommentsImpl clone = comments.clone();
		clone.addComment("Nicola", "Commento3");
		assertEquals(comments.getComments().size(), 2);
		assertEquals(clone.getComments().size(), 3);
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws IOException
	{
		PostCommentsImpl comments = new PostCommentsImpl();
		comments.addComment("Giulio", "Commento1");
		comments.addComment("Luigi", "Commento2");
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(comments); } );
		
		byte[] data = SerializerWrapper.serialize(comments);
		assertDoesNotThrow(() -> { PostCommentsImpl c = SerializerWrapper.deserialize(data, PostCommentsImpl.class); } );
		
		PostCommentsImpl c = SerializerWrapper.deserialize(data, PostCommentsImpl.class);
		assertEquals(c.getComments().size(), 2);
		assertEquals(c.getComments().get(0).username, "Giulio");
		assertEquals(c.getComments().get(0).comment, "Commento1");
		assertEquals(c.getComments().get(1).username, "Luigi");
		assertEquals(c.getComments().get(1).comment, "Commento2");
	}
}

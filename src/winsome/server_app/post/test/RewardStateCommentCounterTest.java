package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.post.RewardStateImpl.CommentCounter;

class RewardStateCommentCounterTest
{
	@Test
	void constructor()
	{
		CommentCounter counter = new CommentCounter(0);
		assertEquals(counter.getCommentCount(), 0);
		
		counter = new CommentCounter(50);
		assertEquals(counter.getCommentCount(), 50);
	}

	@Test
	void checkAddComment()
	{
		CommentCounter counter = new CommentCounter(10);
		counter.addComment();
		assertEquals(counter.getCommentCount(), 11);
	}
	
	@Test
	void checkClone()
	{
		CommentCounter counter = new CommentCounter(10);
		CommentCounter clone = counter.clone();
		
		clone.addComment();
		clone.addComment();
		assertEquals(clone.getCommentCount(), 12);
		assertEquals(counter.getCommentCount(), 10);
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws IOException
	{
		CommentCounter counter = new CommentCounter(10);
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(counter); } );
		
		byte[] data = SerializerWrapper.serialize(counter);
		assertDoesNotThrow(() -> { CommentCounter c = SerializerWrapper.deserialize(data, CommentCounter.class); } );
		
		CommentCounter c = SerializerWrapper.deserialize(data, CommentCounter.class);
		assertEquals(c.getCommentCount(), counter.getCommentCount());
	}
}

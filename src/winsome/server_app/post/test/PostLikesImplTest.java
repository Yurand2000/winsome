package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.post.PostLikesImpl;
import winsome.server_app.post.exceptions.CannotRateException;

class PostLikesImplTest
{
	@Test
	void constructors()
	{
		PostLikesImpl default_constructed = new PostLikesImpl();		
		assertEquals(default_constructed.getLikes(), 0);
		assertEquals(default_constructed.getDislikes(), 0);
		assertTrue(default_constructed.canRate("Giulio"));
		assertTrue(default_constructed.canRate("Caio"));
		assertTrue(default_constructed.canRate("Nicola"));
		assertTrue(default_constructed.canRate("Luigi"));
		
		String[] who_liked = new String[] {"Giulio", "Caio", "Nicola"};
		PostLikesImpl param_constructed =
			new PostLikesImpl(1, 2, new HashSet<>(Arrays.asList(who_liked)));	
		assertEquals(param_constructed.getLikes(), 1);
		assertEquals(param_constructed.getDislikes(), 2);
		assertFalse(param_constructed.canRate("Giulio"));
		assertFalse(param_constructed.canRate("Caio"));
		assertFalse(param_constructed.canRate("Nicola"));
		assertTrue(param_constructed.canRate("Luigi"));
	}
	
	@Test
	void testAddLike()
	{
		PostLikesImpl likes = new PostLikesImpl();
		assertEquals(likes.getLikes(), 0);
		assertEquals(likes.getDislikes(), 0);
		assertTrue(likes.canRate("Nicola"));
		
		likes.addLike("Nicola");
		assertEquals(likes.getLikes(), 1);
		assertEquals(likes.getDislikes(), 0);
		assertFalse(likes.canRate("Nicola"));
		
		assertThrows(CannotRateException.class, () -> { likes.addLike("Nicola"); } );
		assertThrows(CannotRateException.class, () -> { likes.addDislike("Nicola"); } );
	}
	
	@Test
	void testAddDislike()
	{
		PostLikesImpl likes = new PostLikesImpl();
		assertEquals(likes.getLikes(), 0);
		assertEquals(likes.getDislikes(), 0);
		assertTrue(likes.canRate("Nicola"));
		
		likes.addDislike("Nicola");
		assertEquals(likes.getLikes(), 0);
		assertEquals(likes.getDislikes(), 1);
		assertFalse(likes.canRate("Nicola"));

		assertThrows(CannotRateException.class, () -> { likes.addLike("Nicola"); } );
		assertThrows(CannotRateException.class, () -> { likes.addDislike("Nicola"); } );
	}
	
	@Test
	void checkClone()
	{
		PostLikesImpl likes = new PostLikesImpl();
		likes.addLike("Luigi");
		likes.addDislike("Luca");
		
		PostLikesImpl clone = likes.clone();
		clone.addLike("Nicola");
		clone.addDislike("Gian");
		assertTrue(likes.canRate("Nicola"));
		assertTrue(likes.canRate("Gian"));
		assertFalse(clone.canRate("Luigi"));
		assertFalse(clone.canRate("Luca"));
		assertEquals(likes.getLikes(), 1);
		assertEquals(likes.getDislikes(), 1);
		assertEquals(clone.getLikes(), 2);
		assertEquals(clone.getDislikes(), 2);
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws IOException
	{
		PostLikesImpl likes = new PostLikesImpl();
		likes.addLike("Luigi");
		likes.addDislike("Luca");
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(likes); } );
		
		byte[] data = SerializerWrapper.serialize(likes);
		assertDoesNotThrow(() -> { PostLikesImpl l = SerializerWrapper.deserialize(data, PostLikesImpl.class); } );
		
		PostLikesImpl l = SerializerWrapper.deserialize(data, PostLikesImpl.class);
		assertFalse(l.canRate("Luigi"));
		assertFalse(l.canRate("Luca"));
		assertEquals(l.getLikes(), 1);
		assertEquals(l.getDislikes(), 1);
	}
}

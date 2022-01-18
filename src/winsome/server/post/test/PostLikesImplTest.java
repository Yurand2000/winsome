package winsome.server.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

import winsome.server.post.PostLikesImpl;

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
	}
}

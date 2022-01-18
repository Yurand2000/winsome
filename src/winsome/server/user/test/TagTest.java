package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import winsome.server.user.Tag;

class TagTest
{
	@Test
	void constructor()
	{
		Tag constructed = new Tag("ciao");
		assertEquals(constructed.tag, "ciao");
	}

	@Test
	void checkEquals()
	{
		Tag tag1 = new Tag("ciao");
		Tag tag2 = new Tag("ciao");
		Tag tag3 = new Tag("ciao 2");
		
		assertTrue(tag1.equals(tag2));
		assertFalse(tag1.equals(tag3));
		assertTrue(tag2.equals(tag1));
		assertFalse(tag2.equals(tag3));
		assertFalse(tag3.equals(tag1));
		assertFalse(tag3.equals(tag2));
	}
	
	@Test
	void checkToString()
	{
		Tag tag = new Tag("ciao");
		assertEquals(tag.toString(), "ciao");
	}
}

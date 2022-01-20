package winsome.server_app.user.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.user.Tag;

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
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws IOException
	{
		Tag tag = new Tag("ciao");
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(tag); } );
		
		byte[] data = SerializerWrapper.serialize(tag);
		assertDoesNotThrow(() -> { Tag t = SerializerWrapper.deserialize(data, Tag.class); } );
		
		Tag t = SerializerWrapper.deserialize(data, Tag.class);
		assertEquals(tag.tag, t.tag);
	}
}

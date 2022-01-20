package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.post.*;

class ContentPostTest
{
	private ContentPost post;
	private Integer postId;
	private Content content;
	private Set<Integer> rewins;
	private PostLikesImpl likes;
	private PostCommentsImpl comments;
	private RewardStateImpl rewardState;

	@BeforeEach
	void makeDefault()
	{
		Integer[] rewins = new Integer[] {11, 15, 18};
		this.postId = 10;
		this.content = new Content("My Post", "Caio", "My Content");
		this.rewins = new HashSet<Integer>(Arrays.asList(rewins));
		this.likes = new PostLikesImpl();
		this.comments = new PostCommentsImpl();
		this.rewardState = new RewardStateImpl();
		post = new ContentPost(postId, content, this.rewins, likes, comments, rewardState);
	}
	
	@Test
	@SuppressWarnings("unused")
	void constructor()
	{
		assertDoesNotThrow(() -> {
			ContentPost post = new ContentPost(5, content, new HashSet<Integer>(),
				new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
		});
	}
	
	@Test
	void getContent()
	{
		Content postContent = post.getContent();
		assertEquals(postContent.title, content.title);
		assertEquals(postContent.author, content.author);
		assertEquals(postContent.content, content.content);
	}
	
	@Test
	void isRewin()
	{
		assertFalse(post.isRewin());
	}
	
	@Test
	void checkClone()
	{
		ContentPost clone = post.clone();

		Content postContent = clone.getContent();
		assertEquals(postContent.title, content.title);
		assertEquals(postContent.author, content.author);
		assertEquals(postContent.content, content.content);
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws Exception
	{
		resetSerializerWrapper();
		SerializerWrapper.addDeserializers(ContentPost.class, PostCommentsImpl.class, PostLikesImpl.class, RewardStateImpl.class);
		
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(post); } );
		
		byte[] data = SerializerWrapper.serialize(post);
		assertDoesNotThrow(() -> { GenericPost p = SerializerWrapper.deserialize(data, GenericPost.class); } );
		
		GenericPost p = SerializerWrapper.deserialize(data, GenericPost.class);
		assertTrue(p.getClass() == ContentPost.class);
		Content postContent = ((ContentPost)p).getContent();
		assertEquals(postContent.title, content.title);
		assertEquals(postContent.author, content.author);
		assertEquals(postContent.content, content.content);
	}
	
	void resetSerializerWrapper() throws Exception
	{
		Field mapper = SerializerWrapper.class.getDeclaredField("mapper");
		mapper.setAccessible(true);
		mapper.set(null, null);
	}
}

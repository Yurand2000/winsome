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

class RewinPostTest
{
	private RewinPost post;
	private Integer postId;
	private Integer originalPostId;
	private String rewinAuthor;
	private Set<Integer> rewins;
	private PostLikesImpl likes;
	private PostCommentsImpl comments;
	private RewardStateImpl rewardState;

	@BeforeEach
	void makeDefault()
	{
		Integer[] rewins = new Integer[] {11, 15, 18};
		this.postId = 10;
		this.originalPostId = 5;
		this.rewinAuthor = "Caio";
		this.rewins = new HashSet<Integer>(Arrays.asList(rewins));
		this.likes = new PostLikesImpl();
		this.comments = new PostCommentsImpl();
		this.rewardState = new RewardStateImpl();
		post = new RewinPost(postId, originalPostId, rewinAuthor, this.rewins, likes, comments, rewardState);
	}
	
	@Test
	@SuppressWarnings("unused")
	void constructor()
	{
		assertDoesNotThrow(() -> {
			RewinPost post = new RewinPost(5, 1, "A", new HashSet<Integer>(),
				new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
		});
	}

	@Test
	void getOriginalPost()
	{
		assertEquals(post.getOriginalPost(), 5);
	}
	
	@Test
	void getAuthor()
	{
		assertEquals(post.getAuthor(), "Caio");
	}
	
	@Test
	void isRewin()
	{
		assertTrue(post.isRewin());
	}
	
	@Test
	void checkClone()
	{
		RewinPost clone = post.clone();
		assertEquals(clone.getAuthor(), "Caio");
		assertEquals(clone.getOriginalPost(), 5);
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws Exception
	{
		resetSerializerWrapper();
		SerializerWrapper.addDeserializers(RewinPost.class, PostCommentsImpl.class, PostLikesImpl.class, RewardStateImpl.class);
		
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(post); } );
		
		byte[] data = SerializerWrapper.serialize(post);
		assertDoesNotThrow(() -> { GenericPost p = SerializerWrapper.deserialize(data, GenericPost.class); } );
		
		GenericPost p = SerializerWrapper.deserialize(data, GenericPost.class);
		assertTrue(p.getClass() == RewinPost.class);
		assertEquals(((RewinPost)p).getAuthor(), "Caio");
		assertEquals(((RewinPost)p).getOriginalPost(), 5);
	}
	
	void resetSerializerWrapper() throws Exception
	{
		Field mapper = SerializerWrapper.class.getDeclaredField("mapper");
		mapper.setAccessible(true);
		mapper.set(null, null);
	}
}

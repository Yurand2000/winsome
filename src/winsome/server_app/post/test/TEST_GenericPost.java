package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import winsome.generic.SerializerWrapper;
import winsome.server_app.post.GenericPost;

class TEST_GenericPost
{
	private GenericPostTestImpl post;
	private Integer postId;
	private Set<Integer> rewins;
	private PostLikesTestImpl likes;
	private PostCommentsTestImpl comments;
	private RewardStateTestImpl rewardState;

	@BeforeEach
	void makeDefault()
	{
		Integer[] rewins = new Integer[] {11, 15, 18};
		this.postId = 10;
		this.rewins = new HashSet<Integer>(Arrays.asList(rewins));
		this.likes = new PostLikesTestImpl();
		this.comments = new PostCommentsTestImpl();
		this.rewardState = new RewardStateTestImpl();
		post = new GenericPostTestImpl(postId, this.rewins,likes, comments, rewardState);
	}
	
	@Test
	@SuppressWarnings("unused")
	void constructor()
	{
		assertDoesNotThrow(() -> {
			GenericPostTestImpl post = new GenericPostTestImpl(0, new HashSet<Integer>(),
				new PostLikesTestImpl(), new PostCommentsTestImpl(), new RewardStateTestImpl());
		});
	}
	
	@Test
	void getRewins()
	{
		Set<Integer> rewins = post.getRewins();
		assertEquals(rewins.size(), 3);
		assertTrue(rewins.contains(new Integer(11)));
		assertTrue(rewins.contains(new Integer(15)));
		assertTrue(rewins.contains(new Integer(18)));
	}
	
	@Test
	void addLike()
	{
		likes.setExpectedUser("Caio");
		rewardState.setExpectedUser("Caio");
		
		post.addLike("Caio");
		
		likes.checkLikeCalled();
		rewardState.checkLikeCalled();
	}
	
	@Test
	void addDislike()
	{
		likes.setExpectedUser("Caio");
		
		post.addDislike("Caio");
		
		likes.checkDislikeCalled();
		rewardState.checkDislikeCalled();
	}
	
	@Test
	void addComment()
	{
		comments.setExpectedComment("Caio", "Commento");
		rewardState.setExpectedUser("Caio");
		
		post.addComment("Caio", "Commento");
		
		comments.checkAddCommentCalled();
		rewardState.checkAddCommentCalled();
	}
	
	@Test
	void addRewin()
	{
		assertFalse(post.getRewins().contains(new Integer(78)));
		post.addRewin(78);
		assertTrue(post.getRewins().contains(new Integer(78)));
	}
	
	@Test
	void removeRewin()
	{
		assertTrue(post.getRewins().contains(new Integer(11)));
		post.removeRewin(11);
		assertFalse(post.getRewins().contains(new Integer(11)));
	}
	
	@Test
	void checkCalculateReward()
	{
		post.calculateReward();
		assertTrue(rewardState.checkCalcRewardCalled());
	}
	
	@Test
	void checkGetPositiveRatings()
	{
		assertEquals(post.getPositiveRatings(), 0);

		likes.setExpectedUser("Luigi");
		rewardState.setExpectedUser("Luigi");
		post.addLike("Luigi");
		
		assertEquals(post.getPositiveRatings(), 1);

		likes.setExpectedUser("Nicola");
		rewardState.setExpectedUser("Nicola");
		post.addLike("Nicola");		
		likes.setExpectedUser("Fabio");
		rewardState.setExpectedUser("Fabio");
		post.addLike("Fabio");
		
		assertEquals(post.getPositiveRatings(), 3);
	}
	
	@Test
	void checkGetNegativeRatings()
	{
		assertEquals(post.getNegativeRatings(), 0);

		likes.setExpectedUser("Luigi");
		post.addDislike("Luigi");
		
		assertEquals(post.getNegativeRatings(), 1);

		likes.setExpectedUser("Nicola");
		post.addDislike("Nicola");
		likes.setExpectedUser("Fabio");
		post.addDislike("Fabio");
		
		assertEquals(post.getNegativeRatings(), 3);
	}
	
	@Test
	void checkGetComments()
	{
		assertTrue(post.getComments().isEmpty());

		comments.setExpectedComment("Fabio", "comment");
		rewardState.setExpectedUser("Fabio");
		post.addComment("Fabio", "comment");
		
		assertFalse(post.getComments().isEmpty());
		assertEquals(post.getComments().get(0).username, "Fabio");

		comments.setExpectedComment("Giulio", "comment");
		rewardState.setExpectedUser("Giulio");
		post.addComment("Giulio", "comment");
		comments.setExpectedComment("Fabio", "comment");
		rewardState.setExpectedUser("Fabio");
		post.addComment("Fabio", "comment");
		
		assertEquals(post.getComments().size(), 3);
		assertEquals(post.getComments().get(0).username, "Fabio");
		assertEquals(post.getComments().get(1).username, "Giulio");
		assertEquals(post.getComments().get(2).username, "Fabio");		
	}
	
	@Test
	void checkClone()
	{
		GenericPostTestImpl clone = post.clone();
		assertEquals(clone.postId, post.postId);
		Set<Integer> rewins = clone.getRewins();
		assertEquals(rewins.size(), 3);
		assertTrue(rewins.contains(new Integer(11)));
		assertTrue(rewins.contains(new Integer(15)));
		assertTrue(rewins.contains(new Integer(18)));
		
		assertTrue(likes.getCloneCalled());
		assertTrue(comments.getCloneCalled());
		assertTrue(rewardState.getCloneCalled());
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws Exception
	{
		resetSerializerWrapper();
		SerializerWrapper.addDeserializers(GenericPostTestImpl.class, PostCommentsTestImpl.class, PostLikesTestImpl.class, RewardStateTestImpl.class);
		
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(post); } );
		
		byte[] data = SerializerWrapper.serialize(post);
		assertDoesNotThrow(() -> { GenericPost p = SerializerWrapper.deserialize(data, GenericPost.class); } );
		
		GenericPost p = SerializerWrapper.deserialize(data, GenericPost.class);
		assertEquals(p.postId, post.postId);
		Set<Integer> rewins = p.getRewins();
		assertEquals(rewins.size(), 3);
		assertTrue(rewins.contains(new Integer(11)));
		assertTrue(rewins.contains(new Integer(15)));
		assertTrue(rewins.contains(new Integer(18)));
	}
	
	void resetSerializerWrapper() throws Exception
	{
		Field mapper = SerializerWrapper.class.getDeclaredField("mapper");
		mapper.setAccessible(true);
		mapper.set(null, null);
	}
}

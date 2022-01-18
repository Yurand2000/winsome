package winsome.server.post.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server.post.RewinPost;
import winsome.server.post.test.genericPost.*;

class RewinPostTest
{
	private RewinPost post;
	private Integer postId;
	private Integer originalPostId;
	private String rewinAuthor;
	private Set<Integer> rewins;
	private PostLikesTestImpl likes;
	private PostCommentsTestImpl comments;
	private RewardStateTestImpl rewardState;

	@BeforeEach
	void makeDefault()
	{
		Integer[] rewins = new Integer[] {11, 15, 18};
		this.postId = 10;
		this.originalPostId = 5;
		this.rewinAuthor = "Caio";
		this.rewins = new HashSet<Integer>(Arrays.asList(rewins));
		this.likes = new PostLikesTestImpl();
		this.comments = new PostCommentsTestImpl();
		this.rewardState = new RewardStateTestImpl();
		post = new RewinPost(postId, originalPostId, rewinAuthor, this.rewins, likes, comments, rewardState);
	}
	
	@Test
	@SuppressWarnings("unused")
	void constructor()
	{
		assertDoesNotThrow(() -> {
			RewinPost post = new RewinPost(5, 1, "A", new HashSet<Integer>(),
				new PostLikesTestImpl(), new PostCommentsTestImpl(), new RewardStateTestImpl());
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
}

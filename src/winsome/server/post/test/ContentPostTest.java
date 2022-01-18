package winsome.server.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server.post.Content;
import winsome.server.post.ContentPost;
import winsome.server.post.test.genericPost.*;

class ContentPostTest
{
	private ContentPost post;
	private Integer postId;
	private Content content;
	private Set<Integer> rewins;
	private PostLikesTestImpl likes;
	private PostCommentsTestImpl comments;
	private RewardStateTestImpl rewardState;

	@BeforeEach
	void makeDefault()
	{
		Integer[] rewins = new Integer[] {11, 15, 18};
		this.postId = 10;
		this.content = new Content("My Post", "Caio", "My Content");
		this.rewins = new HashSet<Integer>(Arrays.asList(rewins));
		this.likes = new PostLikesTestImpl();
		this.comments = new PostCommentsTestImpl();
		this.rewardState = new RewardStateTestImpl();
		post = new ContentPost(postId, content, this.rewins, likes, comments, rewardState);
	}
	
	@Test
	@SuppressWarnings("unused")
	void constructor()
	{
		assertDoesNotThrow(() -> {
			ContentPost post = new ContentPost(5, content, new HashSet<Integer>(),
				new PostLikesTestImpl(), new PostCommentsTestImpl(), new RewardStateTestImpl());
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
}

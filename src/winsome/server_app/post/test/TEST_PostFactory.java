package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import winsome.server_app.post.Content;
import winsome.server_app.post.ContentPost;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostCommentsImpl;
import winsome.server_app.post.PostFactory;
import winsome.server_app.post.PostLikesImpl;
import winsome.server_app.post.RewardStateImpl;
import winsome.server_app.post.RewinPost;

class TEST_PostFactory
{
	@Test
	void testConstructor()
	{
		assertDoesNotThrow(() -> new PostFactory());

		List<GenericPost> posts = makePostsList();		
		assertDoesNotThrow(() -> new PostFactory(posts));
	}
	
	@Test
	void testMakeContentPost()
	{
		PostFactory factory = new PostFactory();
		ContentPost post = (ContentPost) factory.makeNewPost("title", "author", "content");
		
		assertEquals(post.content.title, "title");
		assertEquals(post.content.author, "author");
		assertEquals(post.content.content, "content");
		
		assertTrue(post.getRewins().isEmpty());
		assertEquals(post.getPositiveRatings(), 0);
		assertEquals(post.getNegativeRatings(), 0);
		assertEquals(post.getComments().size(), 0);
	}
	
	@Test
	void testMakeRewinPost()
	{
		PostFactory factory = new PostFactory();
		RewinPost post = (RewinPost) factory.makeRewinPost(50, "author");
		
		assertEquals(post.getOriginalPostId(), 50);
		assertEquals(post.getAuthor(), "author");
		
		assertTrue(post.getRewins().isEmpty());
		assertEquals(post.getPositiveRatings(), 0);
		assertEquals(post.getNegativeRatings(), 0);
		assertEquals(post.getComments().size(), 0);
	}
	
	@Test
	void testFirstPostHasIdOne()
	{
		PostFactory factory = new PostFactory();
		GenericPost post = factory.makeRewinPost(50, "author");
		
		assertEquals(post.postId, 1);
	}
	
	@Test
	void testMakePostsFromEmptyFactory()
	{
		PostFactory factory = new PostFactory();
		
		GenericPost post = factory.makeRewinPost(50, "author");		
		assertEquals(post.postId, 1);
		post = factory.makeRewinPost(50, "author");		
		assertEquals(post.postId, 2);
		post = factory.makeRewinPost(50, "author");		
		assertEquals(post.postId, 3);
	}
	
	@Test
	void testMakePostsAfterErasing()
	{
		PostFactory factory = new PostFactory();
		
		GenericPost post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		
		factory.signalPostDeleted(2);

		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 2);
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 4);
	}
	
	@Test
	void testMakePostsAfterErasing2()
	{
		PostFactory factory = new PostFactory();
		
		GenericPost post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		post = factory.makeRewinPost(50, "author");
		
		factory.signalPostDeleted(2);
		factory.signalPostDeleted(5);

		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 2);

		factory.signalPostDeleted(1);
		
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 1);
		
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 5);
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 8);
	}
	
	@Test
	void testMakePostsFromParamConstructedFactory()
	{
		PostFactory factory = new PostFactory(makePostsList());
		
		GenericPost post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 2);
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 5);
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 6);
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 8);
		post = factory.makeRewinPost(50, "author");
		assertEquals(post.postId, 10);
	}

	private List<GenericPost> makePostsList()
	{
		return Arrays.asList(new GenericPost[]
		{
			new ContentPost(1, new Content("T", "A", "C"), new HashSet<Integer>(), new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl()),
			new ContentPost(3, new Content("T2", "A", "C"), new HashSet<Integer>(), new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl()),
			new RewinPost(4, 1, "B", new HashSet<Integer>(), new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl()),
			new RewinPost(7, 2, "B", new HashSet<Integer>(), new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl()),
			new ContentPost(9, new Content("T4", "A", "C"), new HashSet<Integer>(), new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl()),
		});
	}
}

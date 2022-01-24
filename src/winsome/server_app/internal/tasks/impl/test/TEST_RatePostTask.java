package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.RatePostRequest;
import winsome.connection.socket_messages.server.RatePostAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.RatePostTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_RatePostTask extends SocketTaskTest
{
	RatePostTask task;
	RatePostRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();

		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "Nicola", UserFactory.makeNewUser("Nicola", "pass", new Tag[]{ new Tag("tag1") }) );
		
		GenericPost post = data.getPostFactory().makeNewPostId(10, "Title", "Nicola", "Content");
		data.getPosts().put(post.postId, post);
		data.getUsers().get("Nicola").addPost(post.postId);
		post = data.getPostFactory().makeNewPostId(11, "Title", "user", "Content");
		data.getPosts().put(post.postId, post);
		data.getUsers().get("user").addPost(post.postId);
		
		
		state.set_user_called = true;
	}
	
	@Test
	void testOnUserNotFollowingAuthor()
	{
		assertFalse(data.getUsers().get("user").getFollowing().contains("Nicola"));
		assertEquals(data.getPosts().get(10).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(10).getNegativeRatings(), 0);
		
		message = new RatePostRequest(10, true);
		task = new RatePostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertEquals(data.getPosts().get(10).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(10).getNegativeRatings(), 0);
	}
	
	@Test
	void testNormalOperationRateUp()
	{
		data.getUsers().get("user").addFollowing("Nicola");
		assertEquals(data.getPosts().get(10).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(10).getNegativeRatings(), 0);
		
		message = new RatePostRequest(10, true);
		task = new RatePostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RatePostAnswer.class);
		assertEquals(data.getPosts().get(10).getPositiveRatings(), 1);
		assertEquals(data.getPosts().get(10).getNegativeRatings(), 0);
	}
	
	@Test
	void testNormalOperationRateDown()
	{
		data.getUsers().get("user").addFollowing("Nicola");
		assertEquals(data.getPosts().get(10).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(10).getNegativeRatings(), 0);
		
		message = new RatePostRequest(10, false);
		task = new RatePostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RatePostAnswer.class);
		assertEquals(data.getPosts().get(10).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(10).getNegativeRatings(), 1);
	}
	
	@Test
	void testOnUserRatingTheirOwnPost()
	{
		assertEquals(data.getPosts().get(11).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(11).getNegativeRatings(), 0);
		
		message = new RatePostRequest(11, true);
		task = new RatePostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertEquals(data.getPosts().get(11).getPositiveRatings(), 0);
		assertEquals(data.getPosts().get(11).getNegativeRatings(), 0);
	}
}

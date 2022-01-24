package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.CommentPostRequest;
import winsome.connection.socket_messages.server.CommentPostAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.CommentPostTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_CommentPostTask extends SocketTaskTest
{
	CommentPostTask task;
	CommentPostRequest message;
	
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
		assertTrue(data.getPosts().get(10).getComments().isEmpty());
		
		message = new CommentPostRequest(10, "ciao mondo");
		task = new CommentPostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertTrue(data.getPosts().get(10).getComments().isEmpty());
	}
	
	@Test
	void testNormalOperation()
	{
		data.getUsers().get("user").addFollowing("Nicola");
		assertTrue(data.getPosts().get(10).getComments().isEmpty());
		
		message = new CommentPostRequest(10, "ciao mondo");
		task = new CommentPostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == CommentPostAnswer.class);
		assertEquals(data.getPosts().get(10).getComments().size(), 1);
		assertEquals(data.getPosts().get(10).getComments().get(0).username, "user");
		assertEquals(data.getPosts().get(10).getComments().get(0).comment, "ciao mondo");
	}
	
	@Test
	void testOnUserRatingTheirOwnPost()
	{
		assertTrue(data.getPosts().get(11).getComments().isEmpty());
		
		message = new CommentPostRequest(11, "ciao mondo");
		task = new CommentPostTask(state, data, message);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertTrue(data.getPosts().get(11).getComments().isEmpty());
	}
}

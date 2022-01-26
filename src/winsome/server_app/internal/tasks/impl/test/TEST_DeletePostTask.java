package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.DeletePostRequest;
import winsome.connection.socket_messages.server.DeletePostAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.DeletePostTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

class TEST_DeletePostTask extends SocketTaskTest
{
	DeletePostTask task;
	DeletePostRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new DeletePostRequest(10);
		task = new DeletePostTask(state, data, message);
		
		state.set_user_called = true;
	}
	
	@Test
	void testOnPostNotOwned()
	{
		User user = UserFactory.makeNewUser("user", "pass", new Tag[] { new Tag("a") });
		data.getUsers().put("user", user);
		User luigi = UserFactory.makeNewUser("Luigi", "pass", new Tag[] { new Tag("a") });
		luigi.addPost(10);
		data.getUsers().put("Luigi", luigi);
		data.getPosts().put(10, data.getPostFactory().makeNewPostId(10, "Title", "Luigi", "Content"));
		assertTrue(data.getPosts().get(10).isNotMarkedForDeletion());
		
		task.run(pool);
		
		assertTrue(data.getPosts().get(10).isNotMarkedForDeletion());
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertFalse(data.getPostFactory().signalPostDelete_called);
		GenericPost post = data.getPosts().get(10);
		assertEquals(post.getAuthor(), "Luigi");
	}
	
	@Test
	void testDeleteContentPost()
	{
		User user = UserFactory.makeNewUser("user", "pass", new Tag[] { new Tag("a") });
		user.addPost(10);
		data.getUsers().put("user", user);
		GenericPost post = data.getPostFactory().makeNewPostId(10, "Title", "user", "Content");
		data.getPosts().put(10, post);
		assertTrue(post.isNotMarkedForDeletion());
		
		task.run(pool);

		assertFalse(post.isNotMarkedForDeletion());
		assertTrue(state.sent_message.getClass() == DeletePostAnswer.class);
		assertTrue(data.getPostFactory().signalPostDelete_called);
		assertTrue(data.getPosts().isEmpty());
	}
	
	@Test
	void testDeleteRewinPost()
	{
		User user = UserFactory.makeNewUser("user", "pass", new Tag[] { new Tag("a") });
		user.addPost(9);
		user.addPost(10);
		data.getUsers().put("user", user);
		GenericPost post = data.getPostFactory().makeNewPostId(9, "Title", "user", "Content");
		post.addRewin(10);
		data.getPosts().put(9, post);
		GenericPost rewin = data.getPostFactory().makeRewinPostId(10, 9, "user");
		data.getPosts().put(10, rewin);

		assertTrue(user.getPosts().contains(9));
		assertTrue(user.getPosts().contains(10));
		assertTrue(data.getPosts().containsKey(9));
		assertTrue(data.getPosts().containsKey(10));
		assertTrue(data.getPosts().get(9).getRewins().contains(10));
		assertTrue(post.isNotMarkedForDeletion());
		assertTrue(rewin.isNotMarkedForDeletion());
		
		task.run(pool);

		assertFalse(rewin.isNotMarkedForDeletion());
		assertTrue(post.isNotMarkedForDeletion());
		assertTrue(state.sent_message.getClass() == DeletePostAnswer.class);
		assertTrue(data.getPostFactory().signalPostDelete_called);
		assertTrue(data.getPostFactory().signalledPosts.contains(10));
		assertTrue(data.getPosts().containsKey(9));
		assertFalse(data.getPosts().containsKey(10));
		assertTrue(data.getPosts().get(9).getRewins().isEmpty());
		assertTrue(user.getPosts().contains(9));
		assertFalse(user.getPosts().contains(10));
	}
	
	@Test
	void testDeletePostRecursive()
	{
		setupTestDeletePostRecursive();
		
		task.run(pool);
		assertTrue(state.sent_message.getClass() == DeletePostAnswer.class);
		assertTrue(data.getPostFactory().signalPostDelete_called);
		assertTrue(data.getPostFactory().signalledPosts.contains(10));
		assertTrue(data.getPostFactory().signalledPosts.contains(13));
		assertTrue(data.getPostFactory().signalledPosts.contains(18));
		assertTrue(data.getPostFactory().signalledPosts.contains(21));
		
		assertTestDeletePostRecursive();
	}
	
	private void setupTestDeletePostRecursive()
	{
		User user = UserFactory.makeNewUser("user", "pass", new Tag[] { new Tag("a") });
		user.addPost(10);
		user.addPost(13);
		User user2 = UserFactory.makeNewUser("user2", "pass", new Tag[] { new Tag("a") });
		user2.addPost(18);
		user2.addPost(21);
		data.getUsers().put("user", user);
		data.getUsers().put("user2", user2);
		
		GenericPost post = data.getPostFactory().makeNewPostId(10, "Title", "user", "Content");
		post.addRewin(13);
		post.addRewin(18);
		data.getPosts().put(10, post);
		post = data.getPostFactory().makeRewinPostId(13, 10, "user");
		post.addRewin(21);
		data.getPosts().put(13, post);
		data.getPosts().put(18, data.getPostFactory().makeRewinPostId(18, 10, "user2"));
		data.getPosts().put(21, data.getPostFactory().makeRewinPostId(21, 13, "user2"));
		
		assertTrue(data.getUsers().get("user").getPosts().contains(10));
		assertTrue(data.getUsers().get("user").getPosts().contains(13));
		assertTrue(data.getUsers().get("user2").getPosts().contains(18));
		assertTrue(data.getUsers().get("user2").getPosts().contains(21));
		
		assertTrue(data.getPosts().containsKey(10));
		assertTrue(data.getPosts().get(10).getRewins().contains(13));
		assertTrue(data.getPosts().get(10).getRewins().contains(18));
		assertTrue(data.getPosts().containsKey(13));
		assertTrue(data.getPosts().get(13).getRewins().contains(21));
		assertTrue(data.getPosts().containsKey(18));
		assertTrue(data.getPosts().containsKey(21));
	}
	
	private void assertTestDeletePostRecursive()
	{
		assertFalse(data.getUsers().get("user").getPosts().contains(10));
		assertFalse(data.getUsers().get("user").getPosts().contains(13));
		assertFalse(data.getUsers().get("user2").getPosts().contains(18));
		assertFalse(data.getUsers().get("user2").getPosts().contains(21));
		
		assertFalse(data.getPosts().containsKey(10));
		assertFalse(data.getPosts().containsKey(13));
		assertFalse(data.getPosts().containsKey(18));
		assertFalse(data.getPosts().containsKey(21));
	}
}

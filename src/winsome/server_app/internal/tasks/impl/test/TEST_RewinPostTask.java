package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.RewinPostRequest;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.connection.socket_messages.server.RewinPostAnswer;
import winsome.server_app.internal.tasks.impl.RewinPostTask;
import winsome.server_app.post.RewinPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_RewinPostTask extends SocketTaskTest
{
	RewinPostTask task;
	RewinPostRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new RewinPostRequest(10);
		task = new RewinPostTask(state, data, message);
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "user2", UserFactory.makeNewUser("user2", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().get("user2").addPost(10);
		data.getPosts().put(10, data.getPostFactory().makeNewPostId(10, "Title", "user2", "Content"));
		
		state.set_user_called = true;
	}
	
	@Test
	void testUserNotFollowed()
	{
		assertFalse(data.getUsers().get("user").getFollowing().contains("user2"));
		assertFalse(data.getUsers().get("user2").getFollowers().contains("user"));
		assertEquals(data.getPosts().size(), 1);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertFalse(data.getPostFactory().makeRewinPost_called);
		assertFalse(data.getPostFactory().makeNewPost_called);
		assertEquals(data.getPosts().size(), 1);
	}
	
	@Test
	void testNormalOperation()
	{
		data.getUsers().get("user").addFollowing("user2");
		data.getUsers().get("user2").addFollower("user");
		assertEquals(data.getPosts().size(), 1);
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == RewinPostAnswer.class);
		RewinPostAnswer answer = (RewinPostAnswer) state.sent_message;
		assertEquals(answer.title, "Title");
		assertEquals(answer.newPostId, 11);
		assertTrue(data.getPostFactory().makeRewinPost_called);
		assertFalse(data.getPostFactory().makeNewPost_called);
		assertEquals(data.getPosts().size(), 2);
		assertTrue(data.getPosts().get(10).getRewins().contains(11));
		assertEquals(data.getPosts().get(11).getAuthor(), "user");
		assertEquals( ((RewinPost)data.getPosts().get(11)).getOriginalPostId(), 10 );
	}
}

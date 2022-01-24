package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.LoginRequest;
import winsome.connection.socket_messages.server.LoginAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.LoginUserTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactory;
import winsome.server_app.post.PostFactoryImpl;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

class TEST_LoginUserTask extends SocketTaskTest
{
	LoginUserTask task;
	LoginRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		message = new LoginRequest("user", "pass");
		task = new LoginUserTask(state, data, message);
	}

	@Test
	void testNormalOperation()
	{
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("car") }) );
		
		task.run(pool);
		
		assertTrue(state.set_user_called);
		assertTrue(state.sent_message.getClass() == LoginAnswer.class);
	}

	@Test
	void testUserDoesNotExist()
	{
		task.run(pool);
		
		assertFalse(state.set_user_called);
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

	@Test
	void testInvalidUserPassword()
	{
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "wrongpass", new Tag[]{ new Tag("car") }) );
		
		task.run(pool);
		
		assertFalse(state.set_user_called);
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}
	
	@Test
	void testReturnsAllUserData()
	{
		prepareReturnsAllUserData();
		data.getWalletUpdater().multicast_address = "multicast_address";
		
		task.run(pool);
		
		assertTrue(state.set_user_called);
		assertTrue(state.sent_message.getClass() == LoginAnswer.class);
		LoginAnswer answer = (LoginAnswer) state.sent_message;
		assertEquals(answer.followed_by_users.length, 2);
		Set<String> followed_by = new HashSet<String>(Arrays.asList(answer.followed_by_users));
		assertTrue(followed_by.contains("Benito"));
		assertTrue(followed_by.contains("Lucia"));
		assertEquals(answer.following_users.length, 1);
		assertEquals(answer.following_users[0], "Nicola");
		assertEquals(answer.my_blog.length, 1);
		assertEquals(answer.my_blog[0].postId, 1);
		assertEquals(answer.my_blog[0].title, "Title");
		assertEquals(answer.udp_multicast_address, "multicast_address");
	}
	
	void prepareReturnsAllUserData()
	{
		PostFactory factory = new PostFactoryImpl();
		GenericPost p = factory.makeNewPost("Title", "user", "Content");
		data.getPosts().put(p.postId, p);
		
		User u = UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("car") });
		u.addFollower("Benito");
		u.addFollower("Lucia");
		u.addFollowing("Nicola");
		u.addPost(p.postId);
		data.getUsers().put("user", u);		
	}
}

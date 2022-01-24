package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.ListUserRequest;
import winsome.connection.socket_messages.server.ListUserAnswer;
import winsome.server_app.internal.tasks.impl.ListUserTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_ListUserTask extends SocketTaskTest
{
	ListUserTask task;
	ListUserRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		message = new ListUserRequest();
		task = new ListUserTask(state, data, message);
		state.set_user_called = true;
	}

	@Test
	void testNoSimilarUsers()
	{
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[] { new Tag("a"), new Tag("b"), new Tag("c") }) );
		
		task.run(pool);

		assertTrue(state.sent_message.getClass() == ListUserAnswer.class);
		ListUserAnswer answer = (ListUserAnswer) state.sent_message;
		assertEquals(answer.similar_users.length, 0);
	}
	
	@Test
	void testNormalOperation()
	{
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[] { new Tag("a"), new Tag("b"), new Tag("c") }) );
		data.getUsers().put( "user1", UserFactory.makeNewUser("user1", "pass", new Tag[] { new Tag("a") }) );
		data.getUsers().put( "user2", UserFactory.makeNewUser("user2", "pass", new Tag[] { new Tag("b") }) );
		data.getUsers().put( "user3", UserFactory.makeNewUser("user3", "pass", new Tag[] { new Tag("f") }) );
		data.getUsers().put( "user4", UserFactory.makeNewUser("user4", "pass", new Tag[] { new Tag("b"), new Tag("f") }) );
		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == ListUserAnswer.class);
		ListUserAnswer answer = (ListUserAnswer) state.sent_message;
		assertEquals(answer.similar_users.length, 3);
		Set<String> similar_users = new HashSet<String>(Arrays.asList(answer.similar_users));
		assertTrue(similar_users.contains("user1"));
		assertTrue(similar_users.contains("user2"));
		assertTrue(similar_users.contains("user4"));
	}
}

package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_LoggedUserTask extends SocketTaskTest
{
	LoggedUserTaskTest task;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		task = new LoggedUserTaskTest(state, data);
		data.getUsers().put("user", UserFactory.makeNewUser("user", "pass", new Tag[] {new Tag("tag")}));
	}

	@Test
	void testNormalOperation()
	{
		state.set_user_called = true;
		
		task.run(pool);
		
		assertTrue(task.executed);
		assertTrue(state.sent_message == null);
		assertTrue(task.user == data.getUsers().get("user"));
	}

	@Test
	void testUserNotLoggedIn()
	{		
		state.set_user_called = false;
		
		task.run(pool);
		
		assertFalse(task.executed);
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}
}

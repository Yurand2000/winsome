package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.LogoutRequest;
import winsome.connection.socket_messages.server.LogoutAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.LogoutUserTask;

class TEST_LogoutUserTask extends SocketTaskTest
{
	LogoutUserTask task;
	LogoutRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		message = new LogoutRequest();
		task = new LogoutUserTask(state, data, message);
	}

	@Test
	void testNormalOperation()
	{
		state.set_user_called = true;
		
		task.run(pool);
		
		assertTrue(state.unset_user_called);
		assertTrue(state.sent_message.getClass() == LogoutAnswer.class);
	}

	@Test
	void testNotLoggedIn()
	{
		task.run(pool);
		
		assertFalse(state.unset_user_called);
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

}

package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.server.RequestExceptionAnswer;

class TEST_SocketClientTask extends SocketTaskTest
{
	SocketClientTaskTest task;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		task = new SocketClientTaskTest(state, data);
	}

	@Test
	void testNormalOperation()
	{
		task.execute_throws = false;
		
		task.run(pool);
		
		assertTrue(task.executed);
		assertTrue(state.sent_message == null);
	}

	@Test
	void testOnException()
	{
		task.execute_throws = true;
		
		task.run(pool);
		
		assertTrue(task.executed);
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

}

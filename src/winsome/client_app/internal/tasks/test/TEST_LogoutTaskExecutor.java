package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.internal.tasks.LogoutTaskExecutor;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.client.LogoutRequest;
import winsome.connection.socket_messages.server.LogoutAnswer;

class TEST_LogoutTaskExecutor extends TaskExecutorTest
{
	private LogoutTaskExecutor task;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		task = new LogoutTaskExecutor();
	}

	@Test
	void testNormalOperation()
	{
		connection.setReceiveMessage(new LogoutAnswer());
		
		task.run(connection, app_api);
		
		app_api.getWalletNotifier().checkUnregisterCalled();
		app_api.getFollowerUpdater().checkUnregisterCalled();		
		assertTrue(connection.sent_message.getClass() == LogoutRequest.class);
		assertTrue(connection.disconnect_called);
	}

	@Test
	void testOnException()
	{
		connection.setReceiveMessage(new Message());
		
		assertThrows(RuntimeException.class, () -> task.run(connection, app_api));
		
		app_api.getWalletNotifier().checkUnregisterCalled();
		app_api.getFollowerUpdater().checkUnregisterCalled();
		assertTrue(connection.disconnect_called);
	}
}

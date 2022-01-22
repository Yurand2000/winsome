package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.socket.tasks.SocketReadTask;
import winsome.server_app.internal.tasks.impl.ParseIncomingMessageTask;

class TEST_SocketReadTask
{
	private SelectorTest selector;
	private SelectionKeyTest key;
	private WinsomeServerTest server;
	private SocketStateTest state;
	
	private SocketReadTask task;
	
	@BeforeEach
	void setup() throws IOException
	{
		server = new WinsomeServerTest();
		selector = new SelectorTest();
		key = new SelectionKeyTest(selector);
		state = new SocketStateTest();
		key.attach(state);
		
		task = new SocketReadTask(key);
	}
	
	@Test
	void testRunNormally() throws InterruptedException
	{
		task.run(server, null);
		
		server.checkExecuteTaskCalled();
		server.checkExpectedTask(ParseIncomingMessageTask.class);
	}
	
	@Test
	void testRunThrowsOnUnknownMessage() throws InterruptedException
	{
		state.getReader().setRetrivedMessageToUnknown();
		
		assertThrows(RuntimeException.class, () ->
		{
			task.run(server, null);
		});
		
		assertEquals(key.interestOps(), SelectionKey.OP_WRITE);
		selector.checkWakeupInvoked();
		
		state.getWriter().checkSendMessageCalled();
	}
}

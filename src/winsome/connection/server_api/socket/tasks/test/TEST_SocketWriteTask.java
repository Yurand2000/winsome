package winsome.connection.server_api.socket.tasks.test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.socket.tasks.SocketWriteTask;

class TEST_SocketWriteTask
{
	private SelectorTest selector;
	private SelectionKeyTest key;
	private WinsomeServerTest server;
	private SocketStateTest state;
	
	private SocketWriteTask task;
	
	@BeforeEach
	void setup() throws IOException
	{
		server = new WinsomeServerTest();
		selector = new SelectorTest();
		key = new SelectionKeyTest(selector);
		state = new SocketStateTest();
		key.attach(state);
		
		task = new SocketWriteTask(key);
	}
	@Test
	void testRun()
	{
		task.run(server, null);
		
		state.getWriter().checkWriteExecuted();
	}
}

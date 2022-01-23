package winsome.connection.server_api.socket.tasks.test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.socket.tasks.SocketWriteTask;

class TEST_SocketWriteTask
{
	private SocketStateTest state;
	private WinsomeDataTest data;
	private ServerThreadpoolTest pool;
	
	private SocketWriteTask task;
	
	@BeforeEach
	void setup() throws IOException
	{
		state = new SocketStateTest();
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		
		task = new SocketWriteTask(state, data);
	}
	@Test
	void testRun()
	{
		task.run(pool);
		
		state.getWriter().checkWriteExecuted();
	}
}

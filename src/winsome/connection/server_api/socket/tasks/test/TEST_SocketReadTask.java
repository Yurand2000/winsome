package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.connection.server_api.socket.tasks.SocketReadTask;
import winsome.server_app.internal.tasks.impl.ParseIncomingMessageTask;

class TEST_SocketReadTask
{
	private SocketStateTest state;
	private WinsomeDataTest data;
	private ServerThreadpoolTest pool;
	
	private SocketReadTask task;
	
	@BeforeEach
	void setup() throws IOException
	{
		state = new SocketStateTest();
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		task = new SocketReadTask(state, data);
	}
	
	@Test
	void testRunNormally() throws InterruptedException
	{
		task.run(pool);
		
		pool.checkEnqueueCalled();
		pool.checkExpectedTask(ParseIncomingMessageTask.class);
	}
	
	@Test
	void testRunThrowsOnUnknownMessage() throws InterruptedException
	{
		state.getReader().setRetrivedMessageToUnknown();
		
		assertThrows(RuntimeException.class, () -> task.run(pool));
		
		state.checkSendAnswerInvoked();
	}
}

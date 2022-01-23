package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

class ServerThreadpoolTest implements ServerThreadpool
{
	private Class<? extends ServerThreadpoolTask> expected_task = null; 
	private boolean enqueue_called = false;

	@Override
	public void enqueueTask(ServerThreadpoolTask new_task)
	{
		expected_task = new_task.getClass();
		enqueue_called = true;
	}

	public void checkExpectedTask(Class<? extends ServerThreadpoolTask> task)
	{
		assertTrue(task == expected_task);
	}
	
	public void checkEnqueueCalled()
	{
		assertTrue(enqueue_called);
		enqueue_called = false;
	}

	@Override
	public void executeTaskNow(ServerThreadpoolTask new_task)
	{
		fail();
	}

}

package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;

class WinsomeServerTest implements WinsomeServer
{
	private boolean execute_task = false;
	private Class<? extends WinsomeTask> expected_task = null; 
	
	public WinsomeServerTest() { }
	public void startServer() { fail(); }
	public void shutdownServer() { fail(); }
	public void saveToFile() { fail(); }
	public void executeTaskNow(WinsomeTask task) { fail(); }

	@Override
	public void executeTask(WinsomeTask task)
	{
		execute_task = true;
		expected_task = task.getClass();
	}

	public void checkExpectedTask(Class<? extends WinsomeTask> task)
	{
		assertTrue(task == expected_task);
	}
	
	public void checkExecuteTaskCalled() throws InterruptedException
	{
		assertTrue(execute_task);
		execute_task = false;
	}
}

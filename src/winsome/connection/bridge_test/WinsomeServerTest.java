package winsome.connection.bridge_test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.tasks.impl.RegisterNewUserTask;

class WinsomeServerTest implements WinsomeServer
{
	private boolean execute_now_called = false;
	
	public void startServer() { fail(); }
	public void shutdownServer() { fail(); }
	public void saveToFile() { fail(); }
	public void executeTask(WinsomeTask task) { fail(); }

	@Override
	public void executeTaskNow(WinsomeTask task) 
	{
		assertTrue(task.getClass() == RegisterNewUserTask.class);
		execute_now_called = true;
	}
	
	public void checkExecuteTaskNowCalled()
	{
		assertTrue(execute_now_called);
		execute_now_called = false;
	}
}

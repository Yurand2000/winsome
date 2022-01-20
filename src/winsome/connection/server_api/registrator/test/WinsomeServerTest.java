package winsome.connection.server_api.registrator.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.tasks.impl.RegisterNewUserTask;

public class WinsomeServerTest implements WinsomeServer
{
	private boolean execute_now_called = false;
	
	@Override
	public void startServer() { fail(); }

	@Override
	public void shutdownServer() { fail(); }

	@Override
	public void saveToFile() { fail(); }

	@Override
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

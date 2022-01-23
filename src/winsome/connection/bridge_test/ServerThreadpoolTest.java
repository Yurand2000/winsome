package winsome.connection.bridge_test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import winsome.server_app.internal.tasks.WinsomeFutureTask;
import winsome.server_app.internal.tasks.impl.RegisterNewUserTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

class ServerThreadpoolTest implements ServerThreadpool
{
	private boolean enqueue_called = false;

	@Override
	public void enqueueTask(ServerThreadpoolTask new_task)
	{
		assertTrue(new_task.getClass() == RegisterNewUserTask.class);
		synchronized(new_task)
		{
			try
			{
				Field done_field = WinsomeFutureTask.class.getDeclaredField("done");
				done_field.setAccessible(true);
				done_field.set(new_task, true);
				new_task.notifyAll();
			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
			{
				fail();
			}			
		}
		enqueue_called = true;
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

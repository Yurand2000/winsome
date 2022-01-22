package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketStateImpl;
import winsome.connection.server_api.socket.tasks.SocketTask;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.tasks.WinsomeTask;

class WinsomeServerTest implements WinsomeServer
{
	private boolean execute_task = false;
	private boolean write_test = false;
	private Class<? extends WinsomeTask> expected_task = null; 

	public WinsomeServerTest() { }
	public void startServer() { fail(); }
	public void shutdownServer() { fail(); }
	public void saveToFile() { fail(); }
	public void executeTaskNow(WinsomeTask task) { fail(); }

	@Override
	public void executeTask(WinsomeTask task)
	{
		expected_task = task.getClass();
		
		fakeExecuteTask(task);
	}

	public void checkExpectedTask(Class<? extends WinsomeTask> task)
	{
		assertTrue(task == expected_task);
	}
	
	public synchronized void waitExecuteTaskCalled() throws InterruptedException
	{
		while(execute_task == false)
		{
			wait();
		}
		execute_task = false;
	}
	
	private void fakeExecuteTask(WinsomeTask task)
	{
		SelectionKey key = getSelectionKey(task);
		SocketStateImpl state = (SocketStateImpl) key.attachment();
		
		if(key.isReadable())
		{
			state.getReader().executeReadOperation();
			
			if(state.getReader().hasMessageBeenRetrived())
			{
				byte[] data = state.getReader().getRetrivedMessage();
				
				assertArrayEquals(data, "read".getBytes());
				
				synchronized(this)
				{
					execute_task = true;
					notifyAll();
				}
				
				if(write_test)
				{
					state.getWriter().addMessageToSend("write".getBytes());
					TaskUtils.setSocketReadyToWrite(key);
				}
			}
		}
		else if(key.isWritable())
		{
			state.getWriter().executeWriteOperation();
		}
	}
	
	public void setWriteTest()
	{
		write_test = true;
	}
	
	private SelectionKey getSelectionKey(WinsomeTask task)
	{
		try
		{
			if(SocketTask.class.isAssignableFrom(task.getClass()))
			{
				Field key_field = SocketTask.class.getDeclaredField("key");
				key_field.setAccessible(true);
				return (SelectionKey) key_field.get(task);
			}
			else
			{
				throw new IllegalArgumentException("unrecognised winsome task");
			}
		}
		catch (NoSuchFieldException | SecurityException |
			IllegalArgumentException | IllegalAccessException e)
		{ throw new RuntimeException(e.toString()); }
	}
}

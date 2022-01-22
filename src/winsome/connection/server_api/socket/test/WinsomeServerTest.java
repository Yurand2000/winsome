package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketState;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.tasks.impl.socket.SocketReadTask;
import winsome.server_app.internal.tasks.impl.socket.SocketWriteTask;

public class WinsomeServerTest implements WinsomeServer
{
	private boolean execute_task = false;
	private boolean write_test = false;
	private Class<? extends WinsomeTask> expected_task = null; 
	
	public WinsomeServerTest()
	{
		
	}
	
	@Override
	public void startServer()
	{
		fail();
	}

	@Override
	public void shutdownServer()
	{
		fail();
	}

	@Override
	public void saveToFile()
	{
		fail();
	}

	@Override
	public void executeTask(WinsomeTask task)
	{
		expected_task = task.getClass();
		
		fakeExecuteTask(task);
	}

	@Override
	public void executeTaskNow(WinsomeTask task)
	{
		fail();
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
		SocketState state = (SocketState) key.attachment();
		
		if(key.isReadable())
		{
			state.reader.executeReadOperation();
			
			if(state.reader.hasMessageBeenRetrived())
			{
				byte[] data = state.reader.getRetrivedMessage();
				
				assertArrayEquals(data, "read".getBytes());
				
				synchronized(this)
				{
					execute_task = true;
					notifyAll();
				}
				
				if(write_test)
				{
					state.writer.addMessageToSend("write".getBytes());
					TaskUtils.setSocketReadyToWrite(key);
				}
			}
		}
		else if(key.isWritable())
		{
			state.writer.executeWriteOperation();
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
			if(task.getClass() == SocketReadTask.class)
			{
				Field key_field = SocketReadTask.class.getDeclaredField("key");
				key_field.setAccessible(true);
				return (SelectionKey) key_field.get(task);
			}
			else if(task.getClass() == SocketWriteTask.class)
			{
				Field key_field = SocketWriteTask.class.getDeclaredField("key");
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

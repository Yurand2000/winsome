package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;

import winsome.connection.server_api.socket.SocketState;
import winsome.connection.server_api.socket.tasks.SocketReadTask;
import winsome.connection.server_api.socket.tasks.SocketTask;
import winsome.connection.socket_messages.Message;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

class ServerThreadpoolTest implements ServerThreadpool
{
	private boolean enqueue_called = false;
	private boolean write_test = false;
	private Class<? extends ServerThreadpoolTask> enqueued_task = null;

	@Override
	public synchronized void enqueueTask(ServerThreadpoolTask new_task)
	{
		enqueued_task = new_task.getClass();
		fakeExecuteTask(new_task);
		enqueue_called = true;
		notifyAll();
	}
	
	public synchronized void waitExecuteTaskCalled() throws InterruptedException
	{
		while(!enqueue_called)
		{
			wait();
		}
	}
	
	public void checkExpectedTask(Class<? extends ServerThreadpoolTask> expected_task)
	{
		assertTrue(enqueued_task == expected_task);
	}
	
	private void fakeExecuteTask(ServerThreadpoolTask task)
	{
		SocketState state = getSocketState(task);
		
		if(isReadOperation(task))
		{
			try { state.getReader().executeReadOperation(); }
			catch (IOException e) { state.cleanupSocketState(); }
			
			if(state.getReader().hasMessageBeenRetrived())
			{
				byte[] data = state.getReader().getRetrivedMessage();
				
				assertArrayEquals(data, "read".getBytes());
				
				if(write_test)
				{
					state.sendAnswerMessage(new Message());
				}
			}
		}
		else
		{
			try { state.getWriter().executeWriteOperation(); }
			catch (IOException e) { state.cleanupSocketState(); }
		}
	}
	
	public boolean isReadOperation(ServerThreadpoolTask task)
	{
		return task.getClass() == SocketReadTask.class;
	}
	
	public void setWriteTest()
	{
		write_test = true;
	}
	
	private SocketState getSocketState(ServerThreadpoolTask task)
	{
		try
		{
			if(SocketTask.class.isAssignableFrom(task.getClass()))
			{
				Field socket_field = SocketTask.class.getDeclaredField("socket");
				socket_field.setAccessible(true);
				return (SocketState) socket_field.get(task);
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

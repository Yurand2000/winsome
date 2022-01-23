package winsome.server_app.internal.tasks;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public abstract class WinsomeFutureTask<T> extends WinsomeTask
{
	private boolean done;
	private T future;
	private RuntimeException exception;
	
	public WinsomeFutureTask(WinsomeData data)
	{
		super(data);
		future = null;
		done = false;
		exception = null;
	}

	@Override
	public final synchronized void run(ServerThreadpool threadpool)
	{
		try
		{
			future = execute(threadpool);
		}
		catch(RuntimeException e)
		{
			exception = e;
		}
		
		done = true;
		notify();
	}
	
	protected abstract T execute(ServerThreadpool threadpool);
	
	public final synchronized T get() throws InterruptedException
	{
		while(!done)
			wait();
		
		if(exception == null)
		{
			return future;
		}
		else
		{
			throw exception;
		}
	}
}

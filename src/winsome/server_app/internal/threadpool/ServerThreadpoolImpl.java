package winsome.server_app.internal.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import winsome.server_app.internal.pausable_threads.PausableThreadFactory;

public class ServerThreadpoolImpl implements ServerThreadpoolControl, ServerThreadpool
{
	private ExecutorService pool;
	private final PausableThreadFactory factory;
	
	public ServerThreadpoolImpl()
	{
		factory = new PausableThreadFactory();
		pool = null;
	}

	@Override
	public void startThreadpool()
	{
		if(pool == null)
		{
			int processors = Runtime.getRuntime().availableProcessors();
			pool = Executors.newFixedThreadPool(processors, factory);
		}
	}

	@Override
	public void stopThreadpool()
	{
		if(pool != null)
		{
			pool.shutdown();
			try { pool.awaitTermination(2, TimeUnit.MINUTES); }
			catch (InterruptedException e) { e.printStackTrace(); }
			pool.shutdownNow();
			pool = null;
		}
	}

	@Override
	public void pauseThreadpool()
	{
		factory.pauseAllExecutions();
	}

	@Override
	public void resumeThreadpool()
	{
		factory.resumeAllExecutions();
	}

	@Override
	public void enqueueTask(ServerThreadpoolTask new_task)
	{
		pool.execute(() -> new_task.run(this));
	}

	@Override
	public void executeTaskNow(ServerThreadpoolTask new_task)
	{
		new_task.run(this);
	}
}

package winsome.server_app.internal.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerThreadpoolImpl implements ServerThreadpoolControl, ServerThreadpool
{
	private ExecutorService pool;
	private final PausableRunnableMonitor monitor;
	
	public ServerThreadpoolImpl()
	{
		monitor = new PausableRunnableMonitor();
		pool = null;
	}

	@Override
	public void startThreadpool()
	{
		if(pool == null)
		{
			int processors = Runtime.getRuntime().availableProcessors();
			pool = Executors.newFixedThreadPool(processors);
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
		monitor.pauseAllThreads();
	}

	@Override
	public void resumeThreadpool()
	{
		monitor.resumeAllThreads();
	}

	@Override
	public void enqueueTask(ServerThreadpoolTask new_task)
	{
		pool.execute(makePausableRunnable(new_task));
	}
	
	private Runnable makePausableRunnable(ServerThreadpoolTask new_task)
	{
		return monitor.makePausableRunnable( () -> new_task.run(this) );
	}
}

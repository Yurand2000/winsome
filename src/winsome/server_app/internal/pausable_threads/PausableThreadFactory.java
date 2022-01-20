package winsome.server_app.internal.pausable_threads;

import java.util.concurrent.ThreadFactory;

public class PausableThreadFactory implements ThreadFactory
{
	private PausableThreadMonitor monitor;	

	public PausableThreadFactory()
	{
		monitor = new PausableThreadMonitor();
	}
	
	@Override
	public Thread newThread(Runnable runnable)
	{
		return new PausableThread(monitor, runnable);
	}

	public void pauseAllExecutions()
	{
		monitor.pauseAllThreads();
	}
	
	public void resumeAllExecutions()
	{
		monitor.resumeAllThreads();
	}
}

package winsome.server_app.internal.threadpool;

public class PausableRunnableMonitor
{
	private boolean threadsPaused;
	
	public PausableRunnableMonitor()
	{
		threadsPaused = false;
	}
	
	public synchronized void pauseAllThreads()
	{
		threadsPaused = true;
	}
	
	public synchronized void resumeAllThreads()
	{
		threadsPaused = false;
		notifyAll();
	}
	
	public synchronized void checkNotPaused() throws InterruptedException
	{
		while(!Thread.currentThread().isInterrupted() && threadsPaused)
		{
			wait();
		}
	}
	
	public Runnable makePausableRunnable(Runnable task)
	{
		return () ->
		{
			try
			{
				this.checkNotPaused();
				task.run();
			}
			catch (InterruptedException e)
			{
				return;
			}
		};
	}
}

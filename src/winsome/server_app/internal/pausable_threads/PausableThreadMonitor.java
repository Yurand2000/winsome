package winsome.server_app.internal.pausable_threads;

public class PausableThreadMonitor
{
	private boolean threadsPaused;
	
	public PausableThreadMonitor()
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
}

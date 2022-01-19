package winsome.server.internal;

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
		while(threadsPaused)
		{
			wait();
		}
	}
}

package winsome.server_app.internal.pausable_threads;

public class PausableThread extends Thread
{
	private PausableThreadMonitor monitor;
	
	public PausableThread(PausableThreadMonitor monitor, Runnable runnable)
	{
		super(runnable);
		this.monitor = monitor;
	}
	
	@Override
	public void run()
	{
		try
		{
			monitor.checkNotPaused();
			super.run();
		}
		catch (InterruptedException e)
		{
			return;
		}
	}
}
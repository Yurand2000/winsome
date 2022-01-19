package winsome.server.internal;

public class WinsomeRunnable implements Runnable
{
	private final WinsomeServer server;
	private final WinsomeData data;
	private final WinsomeTask task;
	
	public WinsomeRunnable(WinsomeServer server, WinsomeData server_data, WinsomeTask task)
	{
		this.server = server;
		this.data = server_data;
		this.task = task;
	}

	@Override
	public void run()
	{
		task.run(server, data);
	}
}

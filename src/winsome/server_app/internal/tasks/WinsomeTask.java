package winsome.server_app.internal.tasks;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

public abstract class WinsomeTask implements ServerThreadpoolTask
{
	protected WinsomeData data;
	
	public WinsomeTask(WinsomeData data)
	{
		this.data = data;
	}
}

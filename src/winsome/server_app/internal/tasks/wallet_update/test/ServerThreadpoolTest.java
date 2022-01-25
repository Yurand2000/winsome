package winsome.server_app.internal.tasks.wallet_update.test;

import java.util.ArrayList;
import java.util.List;

import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

class ServerThreadpoolTest implements ServerThreadpool
{
	public List<ServerThreadpoolTask> enqueued_tasks = new ArrayList<ServerThreadpoolTask>();

	@Override
	public void enqueueTask(ServerThreadpoolTask new_task)
	{
		enqueued_tasks.add(new_task);
	}

}

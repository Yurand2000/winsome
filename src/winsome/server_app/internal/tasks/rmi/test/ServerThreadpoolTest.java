package winsome.server_app.internal.tasks.rmi.test;

import static org.junit.jupiter.api.Assertions.fail;

import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

class ServerThreadpoolTest implements ServerThreadpool
{
	@Override
	public void enqueueTask(ServerThreadpoolTask new_task) { fail(); }
}

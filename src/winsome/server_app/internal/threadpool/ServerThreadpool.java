package winsome.server_app.internal.threadpool;

public interface ServerThreadpool
{
	void enqueueTask(ServerThreadpoolTask new_task);
	void executeTaskNow(ServerThreadpoolTask new_task);
}

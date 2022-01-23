package winsome.server_app.internal.threadpool;

public interface ServerThreadpoolControl
{
	void startThreadpool();
	void stopThreadpool();
	void pauseThreadpool();
	void resumeThreadpool();
}

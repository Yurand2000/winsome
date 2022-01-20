package winsome.server_app.internal;

import winsome.server_app.internal.tasks.WinsomeTask;

public interface WinsomeServer
{
	public void startServer();	
	public void shutdownServer();	
	public void saveToFile();
	public void executeTask(WinsomeTask task);
	public void executeTaskNow(WinsomeTask task);
}

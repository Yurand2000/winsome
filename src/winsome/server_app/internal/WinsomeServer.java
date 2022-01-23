package winsome.server_app.internal;

import winsome.server_app.internal.threadpool.ServerThreadpool;

public interface WinsomeServer
{
	public WinsomeData getWinsomeData();
	public ServerSettings getSettings();
	public ServerThreadpool getThreadpool();
}

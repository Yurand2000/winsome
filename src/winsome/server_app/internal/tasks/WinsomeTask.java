package winsome.server_app.internal.tasks;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;

public interface WinsomeTask
{
	public void run(WinsomeServer server, WinsomeData server_data);
}

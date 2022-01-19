package winsome.server.internal;

import java.util.Timer;
import java.util.TimerTask;

public class ServerAutosaver extends TimerTask
{
	private final Timer timer;
	private final WinsomeServer server;

	public ServerAutosaver(WinsomeServer server)
	{
		this.timer = new Timer();
		this.server = server;
	}
	
	public void startAutosaver()
	{
		timer.scheduleAtFixedRate(this, 0, 300000);
	}
	
	public void stopAutosaver()
	{
		timer.cancel();
	}
	
	@Override
	public void run()
	{
		server.saveToFile();
	}
}

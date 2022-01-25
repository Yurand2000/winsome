package winsome.server_app.internal;

import java.util.Timer;
import java.util.TimerTask;

public class ServerAutosaver extends TimerTask
{
	private final Timer timer;
	private final WinsomeServerImpl server;

	public ServerAutosaver(WinsomeServerImpl server)
	{
		this.timer = new Timer();
		this.server = server;
	}
	
	public void startAutosaver()
	{
		timer.scheduleAtFixedRate(this, 300000, 300000);
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

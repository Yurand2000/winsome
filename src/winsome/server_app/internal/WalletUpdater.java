package winsome.server_app.internal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import winsome.server_app.internal.tasks.impl.CalculatePostsRewardTask;

public class WalletUpdater extends TimerTask
{
	private final WinsomeServer server;
	private final Timer timer;
	private final long timer_period;
	
	public WalletUpdater(WinsomeServer server, long time, TimeUnit unit)
	{
		this.server = server;
		timer = new Timer();
		timer_period = timeUnitToMillis(time, unit);
	}
	
	public void startUpdater()
	{
		timer.scheduleAtFixedRate(this, 0, timer_period);
	}
	
	public void stopUpdater()
	{
		timer.cancel();
	}

	@Override
	public void run()
	{
		server.executeTask(new CalculatePostsRewardTask());
	}
	
	private long timeUnitToMillis(long time, TimeUnit unit)
	{
		return unit.toMillis(time);
	}
}

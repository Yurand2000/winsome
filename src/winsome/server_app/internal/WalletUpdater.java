package winsome.server_app.internal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import winsome.server_app.internal.tasks.wallet_update.CalculatePostsRewardTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class WalletUpdater extends TimerTask
{
	private final WinsomeData winsome_data;
	private final ServerThreadpool threadpool;
	private final Timer timer;
	private final long timer_period;
	
	public WalletUpdater(WinsomeData winsome_data, ServerThreadpool threadpool, long time, TimeUnit unit)
	{
		this.winsome_data = winsome_data;
		this.threadpool = threadpool;
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
		threadpool.enqueueTask(new CalculatePostsRewardTask(winsome_data));
	}
	
	private long timeUnitToMillis(long time, TimeUnit unit)
	{
		return unit.toMillis(time);
	}
}

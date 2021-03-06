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
	private final String author_part;
	
	public WalletUpdater(WinsomeData winsome_data, ServerThreadpool threadpool, Long time, TimeUnit unit, String author_part)
	{
		checkArguments(time, author_part);
		this.winsome_data = winsome_data;
		this.threadpool = threadpool;
		this.author_part = author_part;
		timer = new Timer();
		timer_period = timeUnitToMillis(time, unit);
	}
	
	private void checkArguments(Long time, String author_part)
	{
		if(time < 0)
		{
			throw new IllegalArgumentException();
		}
		
		Double author_part_number = Double.parseDouble(author_part);
		if(author_part_number < 0 || author_part_number > 1)
		{
			throw new IllegalArgumentException();
		}
	}
	
	public void startUpdater()
	{
		timer.scheduleAtFixedRate(this, timer_period, timer_period);
	}
	
	public void stopUpdater()
	{
		timer.cancel();
	}

	@Override
	public void run()
	{
		threadpool.enqueueTask(new CalculatePostsRewardTask(winsome_data, author_part));
	}
	
	private long timeUnitToMillis(Long time, TimeUnit unit)
	{
		return unit.toMillis(time);
	}
}

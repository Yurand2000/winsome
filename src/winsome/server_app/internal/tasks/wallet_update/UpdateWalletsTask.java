package winsome.server_app.internal.tasks.wallet_update;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class UpdateWalletsTask extends WinsomeTask
{
	private final Map<String, AtomicLong> rewards;
	
	public UpdateWalletsTask(WinsomeData data, Map<String, AtomicLong> rewards)
	{
		super(data);
		this.rewards = rewards;
	}

	@Override
	public void run(ServerThreadpool pool)
	{
		AtomicInteger total_wallet_updates = new AtomicInteger(rewards.size());
		for(Map.Entry<String, AtomicLong> entry : rewards.entrySet())
		{
			pool.enqueueTask(new UpdateWalletTask( data, entry.getKey(), entry.getValue().get(), total_wallet_updates ));
		}
	}

}

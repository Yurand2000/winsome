package winsome.server.internal.tasks;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import winsome.server.internal.WinsomeData;
import winsome.server.internal.WinsomeServer;
import winsome.server.internal.WinsomeTask;

public class UpdateWalletsTask implements WinsomeTask
{
	private final ConcurrentMap<String, AtomicLong> rewards;
	
	public UpdateWalletsTask(ConcurrentMap<String, AtomicLong> rewards)
	{
		this.rewards = rewards;
	}

	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		AtomicInteger total_wallet_updates = new AtomicInteger(rewards.size());
		for(Map.Entry<String, AtomicLong> entry : rewards.entrySet())
		{
			server.executeTask(new UpdateWalletTask( entry.getKey(), entry.getValue().get(), total_wallet_updates ));
		}
	}

}

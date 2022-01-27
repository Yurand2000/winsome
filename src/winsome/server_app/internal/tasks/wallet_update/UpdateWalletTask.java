package winsome.server_app.internal.tasks.wallet_update;

import java.util.concurrent.atomic.AtomicInteger;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public class UpdateWalletTask extends WinsomeTask
{
	private final String username;
	private final Long reward;
	private final AtomicInteger total_wallet_updates;
	
	public UpdateWalletTask(WinsomeData data, String username, Long reward, AtomicInteger total_wallet_updates)
	{
		super(data);
		this.username = username;
		this.reward = reward;
		this.total_wallet_updates = total_wallet_updates;
	}

	@Override
	public void run(ServerThreadpool pool)
	{
		updateWallet();
		updateOperationCounter();
	}
	
	private void updateWallet()
	{
		User user = TaskUtils.getUser(username, data);
		TaskUtils.lockUser(user, () -> { user.getWallet().addTransaction(reward); });
	}
	
	private void updateOperationCounter()
	{
		if(total_wallet_updates.decrementAndGet() == 0)
		{
			data.getWalletUpdater().notifyWalletUpdated();
		}
	}
}

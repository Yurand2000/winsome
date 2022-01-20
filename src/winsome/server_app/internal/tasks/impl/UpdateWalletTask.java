package winsome.server_app.internal.tasks.impl;

import java.util.concurrent.atomic.AtomicInteger;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.user.User;

public class UpdateWalletTask implements WinsomeTask
{
	private final String username;
	private final Long reward;
	private final AtomicInteger total_wallet_updates;
	
	public UpdateWalletTask(String username, Long amount, AtomicInteger total_wallet_updates)
	{
		this.username = username;
		this.reward = amount;
		this.total_wallet_updates = total_wallet_updates;
	}

	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		User user = server_data.users.get(username);
		WinsomeData.lockUser(user, () -> { user.wallet.addTransaction(reward); });
		
		if(total_wallet_updates.decrementAndGet() == 0)
		{
			
		}
	}

}

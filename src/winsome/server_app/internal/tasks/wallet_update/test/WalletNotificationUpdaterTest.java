package winsome.server_app.internal.tasks.wallet_update.test;

import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;

class WalletNotificationUpdaterTest implements WalletNotificationUpdater
{
	public boolean notifyWalletUpdated_called = false;
	
	@Override
	public void notifyWalletUpdated()
	{
		notifyWalletUpdated_called = true;
	}

	@Override
	public String getMulticastAddress()
	{
		return null;
	}

}

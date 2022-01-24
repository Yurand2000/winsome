package winsome.server_app.internal.tasks.impl.test;

import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;

class WalletNotificationUpdaterTest implements WalletNotificationUpdater
{
	public String multicast_address;

	@Override
	public void notifyWalletUpdated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMulticastAddress()
	{
		return multicast_address;
	}

}

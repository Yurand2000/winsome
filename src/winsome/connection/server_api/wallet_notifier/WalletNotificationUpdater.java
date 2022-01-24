package winsome.connection.server_api.wallet_notifier;

public interface WalletNotificationUpdater
{
	public void notifyWalletUpdated();
	public String getMulticastAddress();
}

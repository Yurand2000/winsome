package winsome.connection.client_api.wallet_notifier;

public interface WalletNotificationUpdater
{
	void registerWalletUpdateNotifications(String address, Integer port, Runnable task);
	void unregisterWalletUpdateNotifications();
}

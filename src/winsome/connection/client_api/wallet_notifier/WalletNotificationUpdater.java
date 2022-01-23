package winsome.connection.client_api.wallet_notifier;

public interface WalletNotificationUpdater
{
	void registerWalletUpdateNotifications(String address, Runnable task);
	void unregisterWalletUpdateNotifications();
}

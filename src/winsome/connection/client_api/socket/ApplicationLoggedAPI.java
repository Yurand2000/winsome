package winsome.connection.client_api.socket;

import java.util.Set;

import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandler;
import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater;

public interface ApplicationLoggedAPI
{
	String getThisUser();
	Set<String> getFollowers();
	Set<String> getFollowing();
	WalletNotificationUpdater getWalletNotifier();
	FollowerUpdaterRMIHandler getFollowerUpdater();
}

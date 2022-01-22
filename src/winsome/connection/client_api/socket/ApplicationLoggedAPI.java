package winsome.connection.client_api.socket;

import java.util.Map;
import java.util.Set;

import winsome.client_app.api.PostShort;
import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandler;
import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater;

public interface ApplicationLoggedAPI
{
	String getThisUser();
	Set<String> getFollowers();
	Set<String> getFollowing();
	Map<Integer, PostShort> getBlog();
	WalletNotificationUpdater getWalletNotifier();
	FollowerUpdaterRMIHandler getFollowerUpdater();
}

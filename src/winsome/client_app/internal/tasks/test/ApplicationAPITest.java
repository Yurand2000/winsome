package winsome.client_app.internal.tasks.test;

import java.util.HashSet;
import java.util.Set;

import winsome.connection.client_api.socket.ApplicationLoggedAPI;

class ApplicationAPITest implements ApplicationLoggedAPI
{
	private Set<String> followers = new HashSet<String>();
	private Set<String> following = new HashSet<String>();
	private WalletNotificationUpdaterTest wallet_notifier;
	private FollowerUpdaterRMIHandlerTest follower_updater;
	
	public ApplicationAPITest()
	{
		wallet_notifier = new WalletNotificationUpdaterTest();
		follower_updater = new FollowerUpdaterRMIHandlerTest();
	}

	@Override
	public String getThisUser()
	{
		return "user";
	}

	@Override
	public Set<String> getFollowers()
	{
		return followers;
	}

	@Override
	public Set<String> getFollowing()
	{
		return following;
	}

	@Override
	public WalletNotificationUpdaterTest getWalletNotifier()
	{
		return wallet_notifier;
	}

	@Override
	public FollowerUpdaterRMIHandlerTest getFollowerUpdater()
	{
		return follower_updater;
	}

}

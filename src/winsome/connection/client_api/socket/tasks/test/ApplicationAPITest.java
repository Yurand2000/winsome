package winsome.connection.client_api.socket.tasks.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import winsome.client_app.api.PostShort;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;

class ApplicationAPITest implements ApplicationLoggedAPI
{
	private Set<String> followers = new HashSet<String>();
	private Set<String> following = new HashSet<String>();
	private Map<Integer, PostShort> blog = new HashMap<Integer, PostShort>();
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
	public Map<Integer, PostShort> getBlog()
	{
		return blog;
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

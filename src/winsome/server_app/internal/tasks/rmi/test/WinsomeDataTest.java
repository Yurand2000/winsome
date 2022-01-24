package winsome.server_app.internal.tasks.rmi.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactory;
import winsome.server_app.user.User;

class WinsomeDataTest implements WinsomeData
{
	private Map<String, User> users = new HashMap<String, User>();

	@Override
	public Map<Integer, GenericPost> getPosts()
	{
		fail();
		return null;
	}

	@Override
	public Map<String, User> getUsers()
	{
		return users;
	}
	
	@Override public FollowerUpdaterRegistratorHandler getFollowerUpdater() { fail(); return null; }
	@Override public WalletNotificationUpdater getWalletUpdater() { fail(); return null; }
	@Override public PostFactory getPostFactory() { fail(); return null; }
}

package winsome.server_app.internal.tasks.wallet_update.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactory;
import winsome.server_app.user.User;

class WinsomeDataTest implements WinsomeData
{
	private Map<Integer, GenericPost> posts = new HashMap<Integer, GenericPost>();
	private Map<String, User> users = new HashMap<String, User>();
	private WalletNotificationUpdaterTest wallet_updater = new WalletNotificationUpdaterTest();

	@Override
	public Map<Integer, GenericPost> getPosts()
	{
		return posts;
	}

	@Override
	public Map<String, User> getUsers()
	{
		return users;
	}
	
	@Override public FollowerUpdaterRegistratorHandler getFollowerUpdater()
	{
		fail();
		return null;
	}
	
	@Override
	public WalletNotificationUpdaterTest getWalletUpdater()
	{
		return wallet_updater;
	}
	
	@Override public PostFactory getPostFactory()
	{
		fail();
		return null;
	}
}

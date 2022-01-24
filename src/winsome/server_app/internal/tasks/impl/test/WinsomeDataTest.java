package winsome.server_app.internal.tasks.impl.test;

import java.util.HashMap;
import java.util.Map;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

class WinsomeDataTest implements WinsomeData
{
	private Map<Integer, GenericPost> posts = new HashMap<Integer, GenericPost>();
	private Map<String, User> users = new HashMap<String, User>();
	private WalletNotificationUpdaterTest wallet_updater = new WalletNotificationUpdaterTest();
	private FollowerUpdaterRegistratorHandlerTest follower_updater = new FollowerUpdaterRegistratorHandlerTest();
	private PostFactoryTest post_factory = new PostFactoryTest();

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
	
	@Override public FollowerUpdaterRegistratorHandlerTest getFollowerUpdater()
	{
		return follower_updater;
	}
	
	@Override
	public WalletNotificationUpdaterTest getWalletUpdater()
	{
		return wallet_updater;
	}
	
	@Override public PostFactoryTest getPostFactory()
	{
		return post_factory;
	}
}

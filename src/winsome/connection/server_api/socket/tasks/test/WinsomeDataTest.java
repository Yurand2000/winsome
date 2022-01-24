package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactory;
import winsome.server_app.user.User;

class WinsomeDataTest implements WinsomeData
{
	@Override public Map<Integer, GenericPost> getPosts() { fail(); return null; }
	@Override public Map<String, User> getUsers() { fail(); return null; }

	@Override public FollowerUpdaterRegistratorHandler getFollowerUpdater() { fail(); return null; }
	@Override public WalletNotificationUpdater getWalletUpdater() { fail(); return null; }
	@Override public PostFactory getPostFactory() { fail(); return null; }
}

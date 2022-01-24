package winsome.server_app.internal;

import java.util.Map;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactory;
import winsome.server_app.user.User;

public interface WinsomeData
{
	Map<Integer, GenericPost> getPosts();
	Map<String, User> getUsers();
	PostFactory getPostFactory();
	FollowerUpdaterRegistratorHandler getFollowerUpdater();
	WalletNotificationUpdater getWalletUpdater();
}

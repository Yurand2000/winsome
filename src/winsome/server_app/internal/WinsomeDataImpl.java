package winsome.server_app.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactory;
import winsome.server_app.post.PostFactoryImpl;
import winsome.server_app.user.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WinsomeDataImpl implements Cloneable, WinsomeData
{	
	@JsonProperty() private final ConcurrentMap<Integer, GenericPost> posts;
	@JsonProperty() private final ConcurrentMap<String, User> users;
	@JsonIgnore() private PostFactory post_factory;
	@JsonIgnore() private FollowerUpdaterRegistratorHandler follower_updater = null;
	@JsonIgnore() private WalletNotificationUpdater wallet_updater = null;
	
	public WinsomeDataImpl()
	{
		posts = new ConcurrentHashMap<Integer, GenericPost>();
		users = new ConcurrentHashMap<String, User>();
		post_factory = null;
	}

	@Override
	@JsonIgnore public Map<Integer, GenericPost> getPosts()
	{
		return posts;
	}

	@Override
	@JsonIgnore public Map<String, User> getUsers()
	{
		return users;
	}

	@Override
	@JsonIgnore public PostFactory getPostFactory()
	{
		return post_factory;
	}

	@Override
	@JsonIgnore public FollowerUpdaterRegistratorHandler getFollowerUpdater()
	{
		return follower_updater;
	}

	@Override
	@JsonIgnore public WalletNotificationUpdater getWalletUpdater()
	{
		return wallet_updater;
	}
	
	public void setUpdaters(FollowerUpdaterRegistratorHandler follower, WalletNotificationUpdater wallet)
	{
		post_factory = new PostFactoryImpl(posts.values());
		follower_updater = follower;
		wallet_updater = wallet;
	}
	
	@Override
	public WinsomeDataImpl clone()
	{
		WinsomeDataImpl clone = new WinsomeDataImpl();
		for(Map.Entry<Integer, GenericPost> entry : posts.entrySet())
		{
			clone.posts.put(entry.getKey(), entry.getValue().clone());
		}
		
		for(Map.Entry<String, User> entry : users.entrySet())
		{
			clone.users.put(entry.getKey(), entry.getValue().clone());
		}
		return clone;
	}
}

package winsome.client_app.internal;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import winsome.client_app.api.LoggedClientAPI;
import winsome.client_app.api.Post;
import winsome.client_app.api.PostShort;
import winsome.client_app.api.User;
import winsome.client_app.api.Wallet;
import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandler;
import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandlerImpl;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater;
import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdaterImpl;

public class ApplicationLoggedAPIImpl implements LoggedClientAPI, ApplicationLoggedAPI
{
	public final String me;
	public final Set<String> followers;
	public final Set<String> following;
	public final Map<Integer, PostShort> blog;
	public final WalletNotificationUpdaterImpl wallet_notifier;
	public final FollowerUpdaterRMIHandlerImpl follower_updater;
	
	public ApplicationLoggedAPIImpl(String server_host, String me) throws IOException, NotBoundException
	{
		this.me = me;
		this.followers = Collections.synchronizedSet(new HashSet<String>());
		this.following = Collections.synchronizedSet(new HashSet<String>());
		this.blog = Collections.synchronizedMap(new HashMap<Integer, PostShort>());
		this.wallet_notifier = new WalletNotificationUpdaterImpl();
		this.follower_updater = new FollowerUpdaterRMIHandlerImpl(server_host, me, followers);
	}

	@Override
	public List<User> listUsers()
	{
		return null;
	}

	@Override
	public List<User> listFollowers()
	{
		List<User> ret_followers = new ArrayList<User>();
		for(String user : followers)
		{
			ret_followers.add(new User(user));
		}
		
		return ret_followers;
	}

	@Override
	public List<User> listFollowing()
	{
		List<User> ret_following = new ArrayList<User>();
		for(String user : following)
		{
			ret_following.add(new User(user));
		}
		
		return ret_following;
	}

	@Override
	public void followUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unfollowUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PostShort> viewBlog() 
	{
		return new ArrayList<PostShort>(blog.values());
	}

	@Override
	public Integer createPost(String title, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PostShort> showFeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Post showPost(Integer postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePost(Integer postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer rewinPost(Integer postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ratePost(Integer postId, boolean rating) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addComment(Integer postId, String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Wallet getWallet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getWalletInBitcoin() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public String getThisUser()
	{
		return me;
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
	public WalletNotificationUpdater getWalletNotifier()
	{		
		return wallet_notifier;
	}

	@Override
	public FollowerUpdaterRMIHandler getFollowerUpdater()
	{
		return follower_updater;
	}

}

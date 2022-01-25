package winsome.client_app.internal;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import winsome.client_app.api.*;
import winsome.client_app.internal.tasks.*;
import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandler;
import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandlerImpl;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater;
import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdaterImpl;

public class ApplicationLoggedAPIImpl implements LoggedClientAPI, ApplicationLoggedAPI
{
	public final String me;
	public final Set<String> followers;
	public final Set<String> following;
	public final WalletNotificationUpdaterImpl wallet_notifier;
	public final FollowerUpdaterRMIHandlerImpl follower_updater;
	private final ConnectionHandler client_connection;
	
	public ApplicationLoggedAPIImpl(String server_host, Integer rmi_port, String me, ConnectionHandler client_connection) throws IOException, NotBoundException
	{
		this.me = me;
		this.followers = Collections.synchronizedSet(new HashSet<String>());
		this.following = Collections.synchronizedSet(new HashSet<String>());
		this.wallet_notifier = new WalletNotificationUpdaterImpl();
		this.follower_updater = new FollowerUpdaterRMIHandlerImpl(server_host, rmi_port, me, followers);
		this.client_connection = client_connection;
	}

	@Override
	public List<User> listUsers()
	{
		List<User> list = new ArrayList<User>();
		ListUserExecutor executor = new ListUserExecutor(list);
		runExecutor(executor);
		return list;
	}

	@Override
	public List<User> listFollowers()
	{
		return cloneList(followers);
	}

	@Override
	public List<User> listFollowing()
	{
		return cloneList(following);
	}
	
	private List<User> cloneList(Collection<String> users)
	{
		List<User> list = new ArrayList<User>();
		for(String user : users)
		{
			list.add(new User(user));
		}
		return list;
	}

	@Override
	public void followUser(String username)
	{
		FollowUserExecutor executor = new FollowUserExecutor(username);
		runExecutor(executor);
	}

	@Override
	public void unfollowUser(String username) 
	{
		UnfollowUserExecutor executor = new UnfollowUserExecutor(username);
		runExecutor(executor);
	}

	@Override
	public List<PostShort> viewBlog() 
	{
		List<PostShort> blog = new ArrayList<PostShort>();
		GetBlogExecutor executor = new GetBlogExecutor(blog);
		runExecutor(executor);
		return blog;
	}

	@Override
	public Integer createPost(String title, String content)
	{
		CreatePostExecutor executor = new CreatePostExecutor(title, content);
		runExecutor(executor);
		return executor.getNewPostId();
	}

	@Override
	public List<PostShort> showFeed()
	{
		List<PostShort> feed = new ArrayList<PostShort>();
		GetFeedExecutor executor = new GetFeedExecutor(feed);
		runExecutor(executor);
		return feed;
	}

	@Override
	public Post showPost(Integer postId)
	{
		GetPostExecutor executor = new GetPostExecutor(postId);
		runExecutor(executor);
		return executor.getRetrivedPost();
	}

	@Override
	public void deletePost(Integer postId)
	{
		DeletePostExecutor executor = new DeletePostExecutor(postId);
		runExecutor(executor);
	}

	@Override
	public Integer rewinPost(Integer postId)
	{
		RewinPostExecutor executor = new RewinPostExecutor(postId);
		runExecutor(executor);
		return executor.getNewPostId();
	}

	@Override
	public void ratePost(Integer postId, boolean rating)
	{
		RatePostExecutor executor = new RatePostExecutor(postId, rating);
		runExecutor(executor);
	}

	@Override
	public void addComment(Integer postId, String comment)
	{
		CommentPostExecutor executor = new CommentPostExecutor(postId, comment);
		runExecutor(executor);
	}

	@Override
	public Wallet getWallet()
	{
		GetWalletExecutor executor = new GetWalletExecutor();
		runExecutor(executor);
		return executor.getRequestedWallet();
	}

	@Override
	public Integer getWalletInBitcoin()
	{
		GetWalletInBitcoinExecutor executor = new GetWalletInBitcoinExecutor();
		runExecutor(executor);
		return executor.getRequestedWalletInBitcoin();
	}

	private void runExecutor(ClientTaskExecutor executor)
	{
		executor.run(client_connection, this);
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

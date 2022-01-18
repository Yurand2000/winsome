package winsome.server.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import winsome.server.user.exceptions.*;
import winsome.server.wallet.Wallet;

public class User
{
	//constant fields
	public final String username;
	public final LoginInformation login;
	public final List<Tag> tags;
	
	//non-constant fields
	private final Set<String> followers;
	private final Set<String> following;
	private final Set<Integer> posts;
	public final Wallet wallet;
	
	@SuppressWarnings("unused")
	private User() { username = null; login = null; tags = null; followers = null; following = null; posts = null; wallet = null; }
	
	public User(String username, LoginInformation login, Tag[] tags,
		Set<String> followers, Set<String> following, Set<Integer> posts, Wallet wallet)
	{
		if(tags.length == 0 || tags.length > 5)
		{
			throw new RuntimeException("Number of tags must be greater than 0 and less or equal than 5");
		}
		
		this.username = username;
		this.login = login;
		this.tags = Collections.unmodifiableList(Arrays.asList(tags));
		this.followers = new HashSet<String>(followers);
		this.following = new HashSet<String>(following);
		this.posts = new HashSet<Integer>(posts);
		this.wallet = wallet;		
	}
	
	public User(String username, LoginInformation login, Tag[] tags, Wallet wallet)
	{
		this(username, login, tags, new HashSet<String>(), new HashSet<String>(), new HashSet<Integer>(), wallet);
	}
	
	public synchronized Set<String> getFollowers()
	{
		return Collections.unmodifiableSet(followers);
	}
	
	public synchronized Set<String> getFollowing()
	{
		return Collections.unmodifiableSet(following);
	}
	
	public synchronized Set<Integer> getPosts()
	{
		return Collections.unmodifiableSet(posts);
	}

	public synchronized void addFollower(String username)
	{
		followers.add(username);
	}
	
	public synchronized void removeFollower(String username)
	{
		followers.remove(username);
	}
	
	public synchronized void addFollowing(String username)
	{
		if(!following.add(username))
		{
			throw new FollowerAlreadyAddedException(username);
		}
	}
	
	public synchronized void removeFollowing(String username)
	{
		if(!following.remove(username))
		{
			throw new UserWasNotFollowedException(username);
		}
	}
	
	public synchronized void addPost(Integer postId)
	{
		posts.add(postId);
	}
	
	public synchronized void deletePost(Integer postId)
	{
		posts.remove(postId);
	}
}

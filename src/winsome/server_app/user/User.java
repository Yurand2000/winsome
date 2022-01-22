package winsome.server_app.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.server_app.user.exceptions.*;
import winsome.server_app.wallet.Wallet;

public class User implements Cloneable
{
	//constant fields
	@JsonProperty() public final String username;
	@JsonProperty() public final LoginInformation login;
	@JsonProperty() public final List<Tag> tags;
	
	//non-constant fields
	@JsonProperty() private final Set<String> followers;
	@JsonProperty() private final Set<String> following;
	@JsonProperty() private final Set<Integer> posts;
	@JsonProperty() public final Wallet wallet;
	
	@SuppressWarnings("unused")
	private User() { username = null; login = null; tags = null; followers = null; following = null; posts = null; wallet = null; }

	private User(User user)
	{
		this(user.username, user.login, user.tags, user.followers, user.following, user.posts, user.wallet.clone());
	}
	
	private User(String username, LoginInformation login, List<Tag> tags,
		Set<String> followers, Set<String> following, Set<Integer> posts, Wallet wallet)
	{
		if(tags.size() == 0 || tags.size() > 5)
		{
			throw new RuntimeException("Number of tags must be greater than 0 and less or equal than 5");
		}
		
		this.username = username;
		this.login = login;
		this.tags = new ArrayList<Tag>(tags);

		this.followers = new HashSet<String>(followers);
		this.following = new HashSet<String>(following);
		this.posts = new HashSet<Integer>(posts);
		this.wallet = wallet;
	}
	
	public User(String username, LoginInformation login, Tag[] tags,
		Set<String> followers, Set<String> following, Set<Integer> posts, Wallet wallet)
	{		
		this(username, login, Arrays.asList(tags), followers, following, posts, wallet);	
	}
	
	public User(String username, LoginInformation login, Tag[] tags)
	{
		this(username, login, tags, new HashSet<String>(), new HashSet<String>(), new HashSet<Integer>(), new Wallet());
	}
	
	@Override
	public synchronized User clone()
	{
		return new User(this);
	}

	public synchronized void addFollower(String username)
	{
		followers.add(username);
	}
	
	public synchronized void removeFollower(String username)
	{
		followers.remove(username);
	}
	
	public synchronized boolean isFollowedBy(String follower)
	{
		return followers.contains(follower);
	}
	
	public synchronized int countFollowers()
	{
		return followers.size();
	}
	
	@JsonIgnore()
	public synchronized ArrayList<String> getFollowers()
	{
		return new ArrayList<String>(followers);
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
	
	public synchronized boolean isFollowing(String user)
	{
		return following.contains(user);
	}
	
	public synchronized int countFollowing()
	{
		return following.size();
	}

	@JsonIgnore()
	public synchronized List<String> getFollowing()
	{
		return new ArrayList<String>(following);
	}

	
	public synchronized void addPost(Integer postId)
	{
		posts.add(postId);
	}
	
	public synchronized void deletePost(Integer postId)
	{
		posts.remove(postId);
	}
	
	public synchronized boolean isAuthorOfPost(Integer post)
	{
		return posts.contains(post);
	}
	
	public synchronized int countPosts()
	{
		return posts.size();
	}

	@JsonIgnore()
	public synchronized List<Integer> getPosts()
	{
		return new ArrayList<Integer>(posts);
	}
}

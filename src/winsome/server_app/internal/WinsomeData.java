package winsome.server_app.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WinsomeData implements Cloneable
{	
	@JsonProperty() public final ConcurrentMap<Integer, GenericPost> posts;
	@JsonProperty() public final ConcurrentMap<String, User> users;
	
	public WinsomeData()
	{
		posts = new ConcurrentHashMap<Integer, GenericPost>();
		users = new ConcurrentHashMap<String, User>();
	}
	
	@Override
	public WinsomeData clone()
	{
		WinsomeData clone = new WinsomeData();
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
	
	public boolean doesPostExist(Integer postId)
	{
		return posts.containsKey(postId);
	}
	
	public static void lockUser(User a, Runnable r)
	{
		synchronized(a)
		{
			r.run();
		}
	}
	
	public static void lockUserThenPost(User a, GenericPost b, Runnable r)
	{
		synchronized(a)
		{
			synchronized(b)
			{
				r.run();
			}
		}
	}
	
	public static void lockTwoUsers(User a, User b, Runnable r)
	{
		int cmp = a.username.compareTo(b.username);
		if(cmp == 0)
			{ throw new RuntimeException("Can't have two users with the same username."); }
		else if(cmp < 0)
			{ lockTwoOrderedUsers(a, b, r); }
		else
			{ lockTwoOrderedUsers(b, a, r); }
	}
	
	private static void lockTwoOrderedUsers(User prec, User succ, Runnable r)
	{
		synchronized(prec)
		{
			synchronized(succ)
			{
				r.run();
			}
		}
	}
}

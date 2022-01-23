package winsome.server_app.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WinsomeDataImpl implements Cloneable, WinsomeData
{	
	@JsonProperty() public final ConcurrentMap<Integer, GenericPost> posts;
	@JsonProperty() public final ConcurrentMap<String, User> users;
	
	public WinsomeDataImpl()
	{
		posts = new ConcurrentHashMap<Integer, GenericPost>();
		users = new ConcurrentHashMap<String, User>();
	}

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

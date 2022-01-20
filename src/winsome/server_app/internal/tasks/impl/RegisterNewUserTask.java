package winsome.server_app.internal.tasks.impl;

import java.util.Arrays;

import winsome.client_app.api.exceptions.TooManyTagsException;
import winsome.client_app.api.exceptions.UsernameAlreadyTakenException;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

public class RegisterNewUserTask implements WinsomeTask
{
	private final String username;
	private final String password;
	private final String[] tags;
	
	public RegisterNewUserTask(String username, String password, String[] tags)
	{
		this.username = username;
		this.password = password;
		this.tags = Arrays.copyOf(tags, tags.length);
	}
	
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		if(server_data.users.containsKey(username))
		{
			throw new UsernameAlreadyTakenException();
		}
		
		User user = UserFactory.makeNewUser(username, password, makeTags(tags));		
		if(server_data.users.putIfAbsent(username, user) != null)
		{
			throw new UsernameAlreadyTakenException();
		}
	}
	
	private Tag[] makeTags(String[] string_tags)
	{
		if(string_tags.length == 0 || string_tags.length > 5)
		{
			throw new TooManyTagsException();
		}
		
		Tag[] tags = new Tag[string_tags.length];
		for(int i = 0; i < string_tags.length; i++)
		{
			tags[i] = new Tag(string_tags[i]);
		}
		return tags;
	}
}

package winsome.server_app.internal.tasks.rmi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import winsome.client_app.api.exceptions.IncorrectFormatException;
import winsome.client_app.api.exceptions.TooManyTagsException;
import winsome.client_app.api.exceptions.UsernameAlreadyTakenException;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.WinsomeFutureTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

public class RegisterNewUserTask extends WinsomeFutureTask<Void>
{
	private final String username;
	private final String password;
	private final String[] tags;
	
	private static final String charset_regex_string = "\\w+";
	private final Pattern charset_regex;
	
	public RegisterNewUserTask(WinsomeData data, String username, String password, String[] tags)
	{
		super(data);
		this.username = username;
		this.password = password;
		this.tags = Arrays.copyOf(tags, tags.length);
		
		this.charset_regex = Pattern.compile(charset_regex_string);
	}
	
	@Override
	public Void execute(ServerThreadpool pool)
	{
		checkArguments();
		checkUsernameIsNotAlreadyTaken(data);
		createUsername(data);
		return null;
	}
	
	private void checkArguments()
	{
		if(!charset_regex.matcher(username).matches() && username.length() >= 4 && username.length() <= 32)
		{
			throw new IncorrectFormatException("Usernames can only have alphanumeric or underscore characters, min 4 and max 32 characters.");
		}
		
		if(!charset_regex.matcher(password).matches() && password.length() >= 4 && password.length() <= 32)
		{
			throw new IncorrectFormatException("Passwords can only have alphanumeric or underscore characters, min 4 and max 32 characters.");
		}
		
		if(tags.length == 0 || tags.length > 5)
		{
			throw new TooManyTagsException();
		}

		Set<String> unique = new HashSet<String>(Arrays.asList(tags));
		if(unique.size() != tags.length)
		{
			throw new IncorrectFormatException("Some of the given tags are duplicated.");
		}
		
		for(String tag : tags)
		{
			if(!charset_regex.matcher(tag).matches() && tag.length() >= 3 && tag.length() <= 16)
			{
				throw new IncorrectFormatException("Tags can only have alphanumeric or underscore characters, min 3 and max 16 characters.");
			}
		}
	}
	
	private void checkUsernameIsNotAlreadyTaken(WinsomeData server_data)
	{
		if(server_data.getUsers().containsKey(username))
		{
			throw new UsernameAlreadyTakenException();
		}
	}
	
	private void createUsername(WinsomeData server_data)
	{
		User user = UserFactory.makeNewUser(username, password, makeTags(tags));		
		if(server_data.getUsers().putIfAbsent(username, user) != null)
		{
			throw new UsernameAlreadyTakenException();
		}
	}
	
	private Tag[] makeTags(String[] string_tags)
	{		
		Tag[] tags = new Tag[string_tags.length];
		for(int i = 0; i < string_tags.length; i++)
		{
			tags[i] = new Tag(string_tags[i]);
		}
		return tags;
	}
}

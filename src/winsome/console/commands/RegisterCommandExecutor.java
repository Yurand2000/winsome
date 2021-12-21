package winsome.console.commands;

import winsome.client.Connection;
import winsome.client.api.User;
import winsome.console.ConsoleCommandExecutor;
import winsome.console.CannotExecuteException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class RegisterCommandExecutor extends ConsoleCommandExecutor
{
	//Regular expression: register {username} {password} {tag} {tag}[0-4]
	private static final String regex_string = 
		"^(?:register) ([^\\s]+) ([^\\s]+) ([^\\s]+)(?: ([^\\s]+))?(?: ([^\\s]+))?(?: ([^\\s]+))?(?: ([^\\s]+))?$";
	private static final String charset_regex_string = "\\w+";
	private final Pattern regex;
	private final Pattern charset_regex;
	
	public RegisterCommandExecutor(ConsoleCommandExecutor next)
	{
		super(next);
		regex = Pattern.compile(regex_string);
		charset_regex = Pattern.compile(charset_regex_string);
	}

	@Override 
	protected boolean canExecute(String line)
	{
		if(line.startsWith("register "))
		{
			Matcher matcher = regex.matcher(line);
			if(!matcher.matches())
			{
				throw new CannotExecuteException("wrong command format. Correct format is: register <username> <password> <tags>. Tags is a list of 1 to 5 strings separated by spaces.");
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected String execute(String line)
	{
		Matcher matcher = regex.matcher(line);
		matcher.find();
		String username = matcher.group(1);
		String password = matcher.group(2);
		User.Tag[] tags = getTags(matcher);
		
		checkRegisterParameters(username, password, tags);
		return callRegister(username, password, tags);
	}
	
	private String callRegister(String username, String password, User.Tag[] tags)
	{
		Connection.getAPI().register(username, password, tags);
		return "Registered successfully with username: " + username + ".";
	}
	
	private User.Tag[] getTags(Matcher matcher)
	{
		ArrayList<User.Tag> tags = new ArrayList<User.Tag>();
		tags.add(new User.Tag(matcher.group(3)));
		addTag(matcher.group(4), tags);
		addTag(matcher.group(5), tags);
		addTag(matcher.group(6), tags);
		addTag(matcher.group(7), tags);		
		return tags.toArray(new User.Tag[0]);
	}
	
	private void addTag(String tag, ArrayList<User.Tag> array)
	{
		if(tag != null && tag != "")
		{
			array.add(new User.Tag(tag));
		}
	}
	
	private void checkRegisterParameters(String username, String password, User.Tag[] tags)
	{
		checkUsername(username);
		checkPassword(password);
		for(int i = 0; i < tags.length; i++)
		{
			checkTag(tags[i]);
		}
	}
	
	private void checkUsername(String username)
	{
		if(!charset_regex.matcher(username).matches())
		{
			throw new CannotExecuteException("username must have only alphanumeric characters and/or underscores.");
		}
	}
	
	private void checkPassword(String password)
	{
		if(!charset_regex.matcher(password).matches())
		{
			throw new CannotExecuteException("password must have only alphanumeric characters and/or underscores.");
		}
		
		if(password.length() < 4 || password.length() > 32)
		{
			throw new CannotExecuteException("password length must be between 4 and 32 characters long");
		}
	}
	
	private void checkTag(User.Tag tag)
	{
		if(!charset_regex.matcher(tag.tag).matches())
		{
			throw new CannotExecuteException("tag must have only alphanumeric characters and/or underscores.");
		}
	}
}


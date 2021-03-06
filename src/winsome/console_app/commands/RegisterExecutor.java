package winsome.console_app.commands;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.CannotExecuteException;
import winsome.console_app.ConsoleCommandExecutor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class RegisterExecutor extends ConsoleCommandExecutor
{
	//Regular expression: register {username} {password} {tag} {tag}[0-4]
	private static final String regex_string = 
		"^(?:register) ([^\\s]+) ([^\\s]+) ([^\\s]+)(?: ([^\\s]+))?(?: ([^\\s]+))?(?: ([^\\s]+))?(?: ([^\\s]+))?$";
	private static final String charset_regex_string = "\\w+";
	private final Pattern regex;
	private final Pattern charset_regex;
	
	public RegisterExecutor(ConsoleCommandExecutor next)
	{
		super(next);
		regex = Pattern.compile(regex_string);
		charset_regex = Pattern.compile(charset_regex_string);
	}

	@Override 
	protected boolean canExecute(String line)
	{
		if(line.contains("register "))
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
		String[] tags = getTags(matcher);
		
		checkRegisterParameters(username, password, tags);
		return callRegister(username, password, tags);
	}
	
	private String callRegister(String username, String password, String[] tags)
	{
		ClientAppAPI.getAPI().register(username, password, tags);
		return "Registered successfully with username: " + username + ".";
	}
	
	private String[] getTags(Matcher matcher)
	{
		ArrayList<String> tags = new ArrayList<String>();
		tags.add(matcher.group(3));
		addTag(matcher.group(4), tags);
		addTag(matcher.group(5), tags);
		addTag(matcher.group(6), tags);
		addTag(matcher.group(7), tags);		
		return tags.toArray(new String[0]);
	}
	
	private void addTag(String tag, ArrayList<String> array)
	{
		if(tag != null && tag != "")
		{
			array.add(tag);
		}
	}
	
	private void checkRegisterParameters(String username, String password, String[] tags)
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
	}
	
	private void checkTag(String tag)
	{
		if(!charset_regex.matcher(tag).matches())
		{
			throw new CannotExecuteException("tag must have only lowercase characters.");
		}
	}
}


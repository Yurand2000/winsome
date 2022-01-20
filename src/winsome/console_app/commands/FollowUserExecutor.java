package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

public class FollowUserExecutor extends ConsoleCommandExecutor
{
	//Regular expression: follow <username>
	private static final String regex_string = "^(?:follow) ([^\\s]+)$";
	private final Pattern regex;
	
	public FollowUserExecutor(ConsoleCommandExecutor next)
	{
		super(next);
		regex = Pattern.compile(regex_string);
	}

	@Override 
	protected boolean canExecute(String line)
	{
		Matcher matcher = regex.matcher(line);
		return matcher.matches();
	}
	
	@Override
	protected String execute(String line)
	{		
		Matcher matcher = regex.matcher(line);
		matcher.find();
		String username = matcher.group(1);
		ClientAppAPI.getLoggedClientAPI().followUser(username);
		return "Now following user \"" + username + "\".";
	}
}

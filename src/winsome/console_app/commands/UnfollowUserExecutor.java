package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

public class UnfollowUserExecutor extends ConsoleCommandExecutor
{
	//Regular expression: unfollow <username>
	private static final String regex_string = "^(?:unfollow) ([^\\s]+)$";
	private final Pattern regex;
	
	public UnfollowUserExecutor(ConsoleCommandExecutor next)
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
		ClientAppAPI.getLoggedClientAPI().unfollowUser(username);
		return "Not following user \"" + username + "\" anymore.";
	}
}

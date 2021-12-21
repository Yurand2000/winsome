package winsome.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import winsome.client.Connection;
import winsome.console.ConsoleCommandExecutor;

public class LogoutExecutor extends ConsoleCommandExecutor
{
	//Regular expression: logout
	private static final String regex_string = "^(?:logout)$";
	private final Pattern regex;
	
	public LogoutExecutor(ConsoleCommandExecutor next)
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
		Connection.getAPI().logout();
		return "Logout successful.";
	}
}
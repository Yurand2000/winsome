package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

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
		ClientAppAPI.getAPI().logout();
		return "Logout successful.";
	}
}
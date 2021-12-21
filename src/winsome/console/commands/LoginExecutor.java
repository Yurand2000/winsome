package winsome.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import winsome.client.Connection;
import winsome.console.CannotExecuteException;
import winsome.console.ConsoleCommandExecutor;

public class LoginExecutor extends ConsoleCommandExecutor
{
	//Regular expression: login {username} {password}
	private static final String regex_string = "^(?:login) ([^\\s]+) ([^\\s]+)$";
	private final Pattern regex;
	
	public LoginExecutor(ConsoleCommandExecutor next)
	{
		super(next);
		regex = Pattern.compile(regex_string);
	}

	@Override 
	protected boolean canExecute(String line)
	{
		if(line.startsWith("login "))
		{
			Matcher matcher = regex.matcher(line);
			if(!matcher.matches())
			{
				throw new CannotExecuteException("wrong command format. Correct format is: login <username> <password>");
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
		
		Connection.getAPI().login(username, password);
		return "Login successful.";
	}
}

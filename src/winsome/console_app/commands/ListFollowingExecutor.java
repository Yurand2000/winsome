package winsome.console_app.commands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.client_app.api.User;
import winsome.console_app.ConsoleCommandExecutor;

public class ListFollowingExecutor extends ConsoleCommandExecutor
{
	//Regular expression: list following
	private static final String regex_string = "^(?:list following)$";
	private final Pattern regex;
	
	public ListFollowingExecutor(ConsoleCommandExecutor next)
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
		List<User> users = ClientAppAPI.getLoggedClientAPI().listFollowing();
		return createDisplayString(users);
	}
	
	private String createDisplayString(List<User> users)
	{
		StringBuilder string = new StringBuilder();
		
		string.append("My followed:");
		if(users.isEmpty())
		{
			string.append(" you are not following anyone.");
		}
		else
		{
			for(User user : users)
			{
				string.append("\n  ");
				string.append(user.username);
			}
		}	
		
		return string.toString();
	}
}

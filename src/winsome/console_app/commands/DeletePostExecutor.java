package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.Connection;
import winsome.console_app.ConsoleCommandExecutor;

public class DeletePostExecutor extends ConsoleCommandExecutor
{
	//Regular expression: delete <id>
	private static final String regex_string = "^(?:delete) (\\d+)$";
	private final Pattern regex;
	
	public DeletePostExecutor(ConsoleCommandExecutor next)
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
		Integer postId = Integer.parseInt(matcher.group(1));
		
		Connection.getLoggedAPI().deletePost(postId);
		return "Successfully deleted post \"" + postId.toString() + "\".";
	}
}
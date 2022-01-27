package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

public class RewinPostExecutor extends ConsoleCommandExecutor
{
	//Regular expression: rewin <id>
	private static final String regex_string = "^(?:rewin) (\\d+)$";
	private final Pattern regex;
	
	public RewinPostExecutor(ConsoleCommandExecutor next)
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
		Integer postId = getPostId(line);
		Integer newPostId = ClientAppAPI.getLoggedClientAPI().rewinPost(postId);
		return "Successfully rewinned post \"" + postId.toString() + "\". New post id is: \"" + newPostId + "\"";
	}
	
	private Integer getPostId(String line)
	{
		Matcher matcher = regex.matcher(line);
		matcher.find();
		return Integer.parseInt(matcher.group(1));
	}
}

package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

public class AddCommentExecutor extends ConsoleCommandExecutor
{
	//Regular expression: comment <id> <comment>
	private static final String regex_string = "^(?:comment) (\\d+) (.+)$";
	private final Pattern regex;
	
	public AddCommentExecutor(ConsoleCommandExecutor next)
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
		String comment = matcher.group(2);
		
		ClientAppAPI.getLoggedClientAPI().addComment(postId, comment);
		return "Successfully commented post \"" + postId.toString() + "\".";
	}
}

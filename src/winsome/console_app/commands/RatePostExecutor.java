package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

public class RatePostExecutor extends ConsoleCommandExecutor
{
	//Regular expression: rate <id> <vote>
	//Vote can be: +1 -1
	private static final String regex_string = "^(?:rate) (\\d+) (\\+1|-1)$";
	private final Pattern regex;
	
	public RatePostExecutor(ConsoleCommandExecutor next)
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
		boolean isPositiveVote = Integer.parseInt(matcher.group(2)) == 1;
		
		ClientAppAPI.getLoggedClientAPI().ratePost(postId, isPositiveVote);
		return "Successfully rated post \"" + postId.toString() + "\".";
	}
}

package winsome.console_app.commands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.client_app.api.PostShort;
import winsome.console_app.ConsoleCommandExecutor;

public class ShowFeedExecutor extends ConsoleCommandExecutor
{
	//Regular expression: show feed
	private static final String regex_string = "^(?:show feed)$";
	private final Pattern regex;
	
	public ShowFeedExecutor(ConsoleCommandExecutor next)
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
		List<PostShort> feed = ClientAppAPI.getLoggedClientAPI().showFeed();
		return createDisplayString(feed);
	}
	
	private String createDisplayString(List<PostShort> feed)
	{
		StringBuilder string = new StringBuilder();
		string.append("My Feed:");
		
		if(feed.isEmpty())
		{
			string.append(" no one you follow has posted anything.");
		}
		else
		{
			for(PostShort post : feed)
			{
				string.append("\n  ");
				string.append(post.toString());
			}
		}
		
		return string.toString();
	}
}

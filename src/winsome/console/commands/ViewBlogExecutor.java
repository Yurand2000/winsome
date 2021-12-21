package winsome.console.commands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client.Connection;
import winsome.client.api.PostShort;
import winsome.console.ConsoleCommandExecutor;

public class ViewBlogExecutor extends ConsoleCommandExecutor
{
	//Regular expression: blog
	private static final String regex_string = "^(?:blog)$";
	private final Pattern regex;
	
	public ViewBlogExecutor(ConsoleCommandExecutor next)
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
		List<PostShort> blog = Connection.getLoggedAPI().viewBlog();
		StringBuilder string = new StringBuilder();
		string.append("My Blog:");
		for(PostShort post : blog)
		{
			string.append("\n  ");
			string.append(post.toString());
		}
		return string.toString();
	}
}

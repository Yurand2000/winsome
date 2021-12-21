package winsome.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client.Connection;
import winsome.console.CannotExecuteException;
import winsome.console.ConsoleCommandExecutor;

public class CreatePostExecutor extends ConsoleCommandExecutor
{
	//Regular expression: post <<title>> <<content>>
	private static final String regex_string = "^(?:post) <(.+)> <(.+)>$";
	private final Pattern regex;
	
	public CreatePostExecutor(ConsoleCommandExecutor next)
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
		String title = matcher.group(1);
		String content = matcher.group(2);
		checkTitle(title);
		checkContent(content);
		
		Integer postId = Connection.getLoggedAPI().createPost(title, content);
		return "Post with id: " + postId.toString() + " created successfully.";
	}
	
	private void checkTitle(String title)
	{
		if(title.length() > 20)
		{
			throw new CannotExecuteException("Given title is too big. Max 20 characters per title.");
		}
	}
	
	private void checkContent(String content)
	{
		if(content.length() > 500)
		{
			throw new CannotExecuteException("Given content is too big. Max 500 characters per post.");
		}
	}
}

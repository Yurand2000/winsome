package winsome.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import winsome.client.Connection;
import winsome.client.api.Post;
import winsome.console.ConsoleCommandExecutor;

public class ShowPostExecutor extends ConsoleCommandExecutor
{
	//Regular expression: show post <id>
	private static final String regex_string = "^(?:show post) (\\d+)$";
	private final Pattern regex;
	
	public ShowPostExecutor(ConsoleCommandExecutor next)
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
		
		Post post = Connection.getLoggedAPI().showPost(postId);
		return postToString(post);
	}

	private static String format_string =
		"Title: %s\n" +
		"%s\n" +
	    "\n" +
		"Positive Ratings: %d\n" +
	    "Negative Ratings: %d\n" +
		"Comments:";
	
	private String postToString(Post post)
	{
		StringBuilder string = new StringBuilder();
		string.append(
			String.format(format_string, post.title, post.content, post.positive_ratings.intValue(), post.negative_ratings.intValue()));
		
		for(Post.Comment comment : post.comments)
		{
			string.append("\n  ");
			string.append(comment.toString());
		}
		return string.toString();
	}
}
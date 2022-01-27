package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.client_app.api.Post;
import winsome.console_app.ConsoleCommandExecutor;

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
		Integer postId = getPostId(line);		
		Post post = ClientAppAPI.getLoggedClientAPI().showPost(postId);
		return printPost(post);
	}
	
	private Integer getPostId(String line)
	{
		Matcher matcher = regex.matcher(line);
		matcher.find();
		return Integer.parseInt(matcher.group(1));
	}
	
	private String printPost(Post post)
	{
		if(post.original_author == null)
		{
			return postToString(post);
		}
		else
		{
			return rewinPostToString(post);
		}
	}

	private static String format_string =
		"\'%s\' by \'%s\'\n" +
		"%s\n" +
		"- Positive Ratings: %d\n" +
	    "- Negative Ratings: %d\n" +
		"- Comments:";
	
	private String postToString(Post post)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(
			String.format(format_string, post.title, post.author,
				post.content, post.positive_ratings.intValue(), post.negative_ratings.intValue())
		);

		appendComments(builder, post);
		return builder.toString();
	}
	
	private static String rewin_format_string =
		"\'%s\' by \'%s\'\n" +
		"Original Post: %d by \'%s\'\n" +
		"%s\n" +
		"- Positive Ratings: %d\n" +
	    "- Negative Ratings: %d\n" +
		"- Comments:";
				
	private String rewinPostToString(Post post)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(
			String.format(rewin_format_string, post.title, post.author, post.original_postId, post.original_author,
				post.content, post.positive_ratings.intValue(), post.negative_ratings.intValue())
		);
		
		appendComments(builder, post);
		return builder.toString();
	}
	
	private void appendComments(StringBuilder builder, Post post)
	{
		for(Post.Comment comment : post.comments)
		{
			builder.append("\n ");
			builder.append(comment.toString());
		}
	}
}
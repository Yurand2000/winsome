package winsome.client_app.api;

import java.util.List;
import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;

public class Post
{
	public final Integer postId;
	public final String author;
	public final String title;
	public final String content;
	public final Integer positive_ratings;
	public final Integer negative_ratings;
	public final List<Comment> comments;
	
	public Post(Integer postId, String author, String title, String content,
			Integer positive_ratings, Integer negative_ratings, List<Comment> comments)
	{
		this.postId = postId;
		this.author = author;
		this.title = title;
		this.content = content;
		this.positive_ratings = positive_ratings;
		this.negative_ratings = negative_ratings;
		this.comments = unmodifiableList(new ArrayList<Comment>(comments));
	}
	
	public static class Comment
	{
		public final String username;
		public final String comment;
		
		public Comment(String username, String comment)
		{
			this.username = username;
			this.comment = comment;
		}
		
		public Comment(Comment comment)
		{
			this(comment.username, comment.comment);
		}
		
		@Override
		public String toString()
		{
			return username + ": " + comment;
		}
	}

	private static String format_string =
		"Post ID: %8d; Title: %s\n" +
	    "- Author: %s\n" +
		"%s\n" +
	    "\n" +
		"Positive Ratings: %d\n" +
	    "Negative Ratings: %d\n" +
		"Comments:";
	
	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		string.append(
			String.format(format_string, postId.intValue(), title, author, content, positive_ratings.intValue(), negative_ratings.intValue()));
		
		for(Post.Comment comment : comments)
		{
			string.append("\n  ");
			string.append(comment.toString());
		}
		return string.toString();
	}
}

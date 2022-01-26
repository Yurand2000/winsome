package winsome.client_app.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;

public class Post implements Comparable<Post>
{
	@JsonProperty public final Integer postId;
	@JsonProperty public final String author;
	@JsonProperty public final Integer original_postId;
	@JsonProperty public final String original_author;
	@JsonProperty public final String title;
	@JsonProperty public final String content;
	@JsonProperty public final Integer positive_ratings;
	@JsonProperty public final Integer negative_ratings;
	@JsonProperty public final List<Comment> comments;
	
	@SuppressWarnings("unused")
	private Post() { postId = 0; original_postId = 0; author = null; original_author = null; title = null; content = null; positive_ratings = null; negative_ratings = null; comments = null; }

	public Post(Integer postId, String author, String title, String content,
			Integer positive_ratings, Integer negative_ratings, List<Comment> comments)
	{
		this(postId, author, 0, null, title, content,
			positive_ratings, negative_ratings, comments);
	}
	
	public Post(Integer postId, String author, Integer orginal_postId, String original_author, String title, String content,
			Integer positive_ratings, Integer negative_ratings, List<Comment> comments)
	{
		this.postId = postId;
		this.author = author;
		this.original_postId = orginal_postId;
		this.original_author = original_author;
		this.title = title;
		this.content = content;
		this.positive_ratings = positive_ratings;
		this.negative_ratings = negative_ratings;
		this.comments = unmodifiableList(new ArrayList<Comment>(comments));
	}

	private static String format_string =
		"Post ID: %d; Title: %s\n" +
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
	
	@Override
	public boolean equals(Object p)
	{
		if(p.getClass() == Post.class)
		{
			Post cast = (Post) p;
			
			return cast.postId == postId &&
				cast.author == author &&
				cast.title == title &&
				cast.content == content &&
				cast.positive_ratings == positive_ratings &&
				cast.negative_ratings == negative_ratings &&
				comments.equals(cast.comments);
		}
		else
		{
			return false;
		}
	}

	@Override
	public int compareTo(Post post)
	{
		return postId.compareTo(post.postId);
	}
	
	public static class Comment
	{
		@JsonProperty() public final String username;
		@JsonProperty() public final String comment;
		
		@SuppressWarnings("unused")
		private Comment() { username = null; comment = null; }
		
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
		
		@Override
		public boolean equals(Object c)
		{
			if(c.getClass() == Post.Comment.class)
			{
				Post.Comment cast = (Post.Comment) c;
				
				return cast.username == username &&
					cast.comment == comment;
			}
			else
			{
				return false;
			}
		}
	}
}

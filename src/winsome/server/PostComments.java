package winsome.server;

import java.util.List;

public interface PostComments
{
	void addComment(String username, String comment);
	List<Comment> getComments();

	public static class Comment
	{
		public final String username;
		public final String comment;
		
		public Comment(String username, String comment)
		{
			this.username = username;
			this.comment = comment;
		}
	}
}

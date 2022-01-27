package winsome.server_app.post;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface PostComments extends Cloneable
{
	void addComment(String username, String comment);
	List<Comment> getComments();	
	PostComments clone();

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
	}
}

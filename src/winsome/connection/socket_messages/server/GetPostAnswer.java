package winsome.connection.socket_messages.server;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.client_app.api.Post;
import winsome.client_app.api.Post.Comment;
import winsome.connection.socket_messages.Message;

@JsonTypeName("get_post_answer")
public class GetPostAnswer extends Message
{
	public final Post post;
	
	@SuppressWarnings("unused")
	private GetPostAnswer() { post = null; }
	
	public GetPostAnswer(Integer postId, String author, String title, String content,
			Integer positive_ratings, Integer negative_ratings, List<Comment> comments)
	{
		this.post = new Post(postId, author, title, content,
			positive_ratings, negative_ratings, comments);
	}
	
	public GetPostAnswer(Post post)
	{
		this.post = post;
	}
}

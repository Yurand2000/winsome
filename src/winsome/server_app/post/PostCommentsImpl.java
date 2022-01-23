package winsome.server_app.post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("post_comments_impl")
public class PostCommentsImpl implements PostComments
{
	@JsonProperty() private final List<Comment> comments;
	
	public PostCommentsImpl()
	{
		comments = new LinkedList<Comment>();
	}
	
	public PostCommentsImpl(List<Comment> comments)
	{
		this.comments = new LinkedList<Comment>(comments);
	}
	
	@Override
	public void addComment(String username, String comment)
	{
		comments.add(new Comment(username, comment));
	}

	@Override
	public List<Comment> getComments()
	{
		return Collections.unmodifiableList(new ArrayList<Comment>(comments));
	}
	
	@Override
	public PostCommentsImpl clone()
	{
		return new PostCommentsImpl(this.comments);
	}
}

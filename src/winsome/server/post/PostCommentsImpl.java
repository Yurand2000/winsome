package winsome.server.post;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostCommentsImpl implements PostComments
{
	private final List<Comment> comments;

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
		return Collections.unmodifiableList(comments);
	}
}

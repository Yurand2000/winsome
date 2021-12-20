package winsome.client.api;

public class PostShort
{
	public final Integer postId;
	public final String author;
	public final String title;
	
	public PostShort(Integer postId, String author, String title)
	{
		this.postId = postId;
		this.author = author;
		this.title = title;
	}
	
	public PostShort(Post post)
	{
		this(post.postId, post.author, post.title);
	}
}

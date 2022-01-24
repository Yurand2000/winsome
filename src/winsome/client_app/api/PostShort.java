package winsome.client_app.api;

public class PostShort implements Comparable<PostShort>
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
	
	@Override
	public String toString()
	{
		return String.format("ID: %8d; Author: %s; Title: %s", postId.intValue(), author, title);
	}
	
	@Override
	public boolean equals(Object p)
	{
		if(p.getClass() == PostShort.class)
		{
			PostShort cast = (PostShort) p;
			
			return cast.postId == postId &&
				cast.author == author &&
				cast.title == title;
		}
		else
		{
			return false;
		}
	}

	@Override
	public int compareTo(PostShort post)
	{
		return postId.compareTo(post.postId);
	}
}

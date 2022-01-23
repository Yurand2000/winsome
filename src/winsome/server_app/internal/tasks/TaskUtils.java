package winsome.server_app.internal.tasks;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.Content;
import winsome.server_app.post.ContentPost;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewinPost;
import winsome.server_app.user.User;

public class TaskUtils
{
	private TaskUtils() { }
	
	public boolean doesPostExist(Integer postId, WinsomeData server_data)
	{
		return server_data.getPosts().containsKey(postId);
	}
	
	public boolean doesUserExist(String username, WinsomeData server_data)
	{
		return server_data.getUsers().containsKey(username);
	}
	
	public static String getPostAuthor(Integer postId, WinsomeData server_data)
	{
		GenericPost post = server_data.getPosts().get(postId);
		return post.getAuthor();
	}
	
	public static Content getPostContent(Integer postId, WinsomeData server_data)
	{
		String author = getPostAuthor(postId, server_data);
		return getPostContent(postId, author, server_data);
	}
	
	private static Content getPostContent(Integer postId, String author, WinsomeData server_data)
	{
		GenericPost post = server_data.getPosts().get(postId);
		
		if(post.isRewin())
		{
			Integer original_postId = ((RewinPost)post).getOriginalPostId();
			
			return getPostContent(original_postId, author, server_data);
		}
		else
		{
			StringBuilder title = new StringBuilder();
			StringBuilder content = new StringBuilder();
			lockPost(post, () -> {
				title.append( ((ContentPost)post).content.title );
				content.append( ((ContentPost)post).content.content );
			});
			
			return new Content(title.toString(), author, content.toString());
		}
	}
	
	
	
	public static void lockUser(User a, Runnable r)
	{
		synchronized(a)
		{
			r.run();
		}
	}
	
	public static void lockPost(GenericPost post, Runnable r)
	{
		synchronized(post)
		{
			r.run();
		}
	}
	
	public static void lockUserThenPost(User a, GenericPost b, Runnable r)
	{
		synchronized(a)
		{
			synchronized(b)
			{
				r.run();
			}
		}
	}
	
	public static void lockTwoUsers(User a, User b, Runnable r)
	{
		int cmp = a.username.compareTo(b.username);
		if(cmp == 0)
			{ throw new RuntimeException("Can't have two users with the same username."); }
		else if(cmp < 0)
			{ lockTwoOrderedUsers(a, b, r); }
		else
			{ lockTwoOrderedUsers(b, a, r); }
	}
	
	private static void lockTwoOrderedUsers(User prec, User succ, Runnable r)
	{
		synchronized(prec)
		{
			synchronized(succ)
			{
				r.run();
			}
		}
	}
}

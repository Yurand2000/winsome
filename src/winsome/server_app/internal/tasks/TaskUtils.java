package winsome.server_app.internal.tasks;

import java.util.HashSet;
import java.util.Set;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.Content;
import winsome.server_app.post.ContentPost;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewinPost;
import winsome.server_app.user.User;

public class TaskUtils
{
	private TaskUtils() { }
	
	public static boolean doesUserExist(String username, WinsomeData server_data)
	{
		return server_data.getUsers().containsKey(username);
	}
	
	public static User getUser(String username, WinsomeData server_data)
	{
		User user = server_data.getUsers().get(username);
		if(user == null)
		{
			throw new RuntimeException("Given user with username " + username + " does not exist.");
		}
		else
		{
			return user;
		}
	}
	
	public static boolean doesPostExist(Integer postId, WinsomeData server_data)
	{
		return server_data.getPosts().containsKey(postId);
	}
	
	public static GenericPost getPost(Integer postId, WinsomeData server_data)
	{
		GenericPost post = server_data.getPosts().get(postId);
		if(post == null)
		{
			throw new RuntimeException("Given post with id " + postId.toString() + " does not exist.");
		}
		else
		{
			return post;
		}
	}
	
	public static String getPostAuthor(Integer postId, WinsomeData server_data)
	{
		GenericPost post = getPost(postId, server_data);
		return post.getAuthor();
	}
	
	public static Content getPostContent(Integer postId, WinsomeData server_data)
	{
		String author = getPostAuthor(postId, server_data);
		return getPostContent(postId, author, server_data);
	}
	
	private static Content getPostContent(Integer postId, String author, WinsomeData server_data)
	{
		GenericPost post = getPost(postId, server_data);
		
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
	
	public static void deletePost(Integer postId, WinsomeData server_data)
	{
		GenericPost post = getPost(postId, server_data);

		Set<Integer> rewins = new HashSet<Integer>();
		lockPost(post, () ->
		{
			rewins.addAll(post.getRewins());
		});
		
		for(Integer rewin : rewins)
		{
			deletePost(rewin, server_data);
		}
		
		User user = getUser(post.getAuthor(), server_data);
		
		if(post.isRewin())
		{
			GenericPost original_post = getPost( ((RewinPost) post).getOriginalPostId(), server_data );
			
			lockUserThenPosts(user, original_post, post, () ->
			{
				user.deletePost(postId);
				original_post.removeRewin(postId);
				server_data.getPosts().remove(postId);
				server_data.getPostFactory().signalPostDeleted(postId);
			});
		}
		else
		{
			lockUserThenPost(user, post, () ->
			{
				user.deletePost(postId);
				server_data.getPosts().remove(postId);
				server_data.getPostFactory().signalPostDeleted(postId);
			});
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
	
	public static void lockUserThenPosts(User a, GenericPost p1, GenericPost p2, Runnable r)
	{
		synchronized(a)
		{
			lockTwoPosts(p1, p2, r);
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
	
	public static void lockTwoPosts(GenericPost a, GenericPost b, Runnable r)
	{
		int cmp = a.postId.compareTo(b.postId);
		if(cmp == 0)
			{ throw new RuntimeException("Can't have two posts with the same username."); }
		else if(cmp < 0)
			{ lockTwoOrderedPosts(a, b, r); }
		else
			{ lockTwoOrderedPosts(b, a, r); }
	}
	
	private static void lockTwoOrderedPosts(GenericPost prec, GenericPost succ, Runnable r)
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

package winsome.server_app.internal.tasks;

import java.util.HashSet;
import java.util.Set;

import winsome.client_app.api.exceptions.PostDoesNotExistException;
import winsome.client_app.api.exceptions.UserDoesNotExistException;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.Content;
import winsome.server_app.post.ContentPost;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewinPost;
import winsome.server_app.user.User;

public class TaskUtils
{
	private TaskUtils() { }
	
	public static User getUser(String username, WinsomeData server_data)
	{
		User user = server_data.getUsers().get(username);
		if(user == null)
		{
			throw new UserDoesNotExistException(username);
		}
		else
		{
			return user;
		}
	}
	
	public static GenericPost getPost(Integer postId, WinsomeData server_data)
	{
		GenericPost post = server_data.getPosts().get(postId);
		if(post == null)
		{
			throw new PostDoesNotExistException(postId);
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
	
	public static Integer getOriginalPostId(Integer postId, WinsomeData server_data)
	{
		GenericPost post = getPost(postId, server_data);
		
		if(post.isRewin())
		{
			Integer original_postId = ((RewinPost)post).getOriginalPostId();	
			return getOriginalPostId( original_postId, server_data );
		}
		else
		{
			return postId;
		}
	}
	
	public static Content getPostContent(Integer postId, WinsomeData server_data)
	{
		GenericPost post = getPost(postId, server_data);
		
		if(post.isRewin())
		{
			Integer original_postId = ((RewinPost)post).getOriginalPostId();			
			return getPostContent( original_postId, server_data );
		}
		else
		{
			return getPostContent( (ContentPost)post );
		}
	}
	
	private static Content getPostContent(ContentPost post)
	{
		StringBuilder title = new StringBuilder();
		StringBuilder author = new StringBuilder();
		StringBuilder content = new StringBuilder();
		lockPost(post, () ->
		{
			title.append( post.content.title );
			author.append( post.content.author );
			content.append( post.content.content );
		});
		
		return new Content(title.toString(), author.toString(), content.toString());
	}
	
	public static void deletePost(Integer postId, WinsomeData server_data)
	{
		GenericPost post = getPost(postId, server_data);

		Set<Integer> rewins = getRewinsAndMarkPostForDeletion(post);
		deleteRewins(rewins, server_data);
		effectivelyDeletePost(post, server_data);
	}
	
	private static Set<Integer> getRewinsAndMarkPostForDeletion(GenericPost post)
	{
		Set<Integer> rewins = new HashSet<Integer>();
		lockPost(post, () ->
		{
			post.markForDeletion();
			rewins.addAll(post.getRewins());
		});
		return rewins;
	}
	
	private static void deleteRewins(Set<Integer> rewins, WinsomeData server_data)
	{
		for(Integer rewin : rewins)
		{
			deletePost(rewin, server_data);
		}
	}
	
	private static void effectivelyDeletePost(GenericPost post, WinsomeData server_data)
	{
		Integer postId = post.postId;
		
		User user = getUser(post.getAuthor(), server_data);
		
		if(post.isRewin())
		{
			GenericPost original_post = getPost( ((RewinPost) post).getOriginalPostId(), server_data );
			
			lockUserThenPosts(user, original_post, post, () ->
			{
				effectivelyDeletePost(user, postId, server_data);
				original_post.removeRewin(postId);
			});
		}
		else
		{
			lockUserThenPost(user, post, () ->
			{
				effectivelyDeletePost(user, postId, server_data);
			});
		}
	}
	
	private static void effectivelyDeletePost(User post_author, Integer postId, WinsomeData server_data)
	{
		post_author.deletePost(postId);
		server_data.getPosts().remove(postId);
		server_data.getPostFactory().signalPostDeleted(postId);
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
			{ throw new RuntimeException("Can't have two posts with the same identifier."); }
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

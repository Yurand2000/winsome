package winsome.server_app.post;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class PostFactory
{
	private Integer current_last_id;
	private final Queue<Integer> sequence_holes;
	
	public PostFactory()
	{
		current_last_id = 0;
		sequence_holes = new PriorityQueue<Integer>();
	}
	
	public GenericPost makeNewPost(String title, String author, String content)
	{
		Integer newPostId = generateNextId();
		Content data = new Content(title, author, content);
		
		return new ContentPost(newPostId, data, new HashSet<Integer>(),
			new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
	}
	
	public GenericPost makeRewinPost(Integer postId, String author)
	{
		Integer newPostId = generateNextId();
		
		return new RewinPost(newPostId, postId, author, new HashSet<Integer>(),
			new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
	}
	
	private synchronized Integer generateNextId()
	{
		if(sequence_holes.isEmpty())
		{
			current_last_id = current_last_id + 1;
			return current_last_id;
		}
		else
		{
			return sequence_holes.remove();
		}
	}
	
	public synchronized void signalPostDeleted(Integer postId)
	{
		sequence_holes.add(postId);
	}
	
	public PostFactory(List<GenericPost> posts)
	{
		current_last_id = findLastInteger(posts);
		Set<Integer> missing = generateMissingSet(current_last_id, posts);
		sequence_holes = new PriorityQueue<Integer>(missing);
	}
	
	private Integer findLastInteger(List<GenericPost> posts)
	{
		Integer max = 0;
		for(GenericPost post : posts)
		{
			if(post.postId > max)
				max = post.postId;
		}
		return max;
	}
	
	private Set<Integer> generateMissingSet(Integer max, List<GenericPost> posts)
	{
		Set<Integer> missing = new TreeSet<Integer>();
		for(int i = 1; i < max - 1; i++)
		{
			missing.add(i);
		}
		
		for(GenericPost post : posts)
		{
			missing.remove(post.postId);
		}
		return missing;
	}
}

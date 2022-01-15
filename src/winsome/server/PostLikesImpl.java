package winsome.server;

import java.util.HashSet;
import java.util.Set;

public class PostLikesImpl implements PostLikes
{
	private int positive_likes;
	private int negative_likes;
	private Set<String> who_liked;
	
	public PostLikesImpl()
	{
		positive_likes = 0;
		negative_likes = 0;
		who_liked = new HashSet<String>();
	}
	
	public PostLikesImpl(int positive_likes, int negative_likes, Set<String> who_liked)
	{
		this.positive_likes = positive_likes;
		this.negative_likes = negative_likes;
		this.who_liked = new HashSet<String>(who_liked);
	}

	@Override
	public void addLike(String username)
	{
		positive_likes++;
		who_liked.add(username);
	}

	@Override
	public void addDislike(String username)
	{
		negative_likes++;
		who_liked.add(username);
	}

	@Override
	public int getLikes()
	{
		return positive_likes;
	}

	@Override
	public int getDislikes()
	{
		return negative_likes;
	}

	@Override
	public boolean canRate(String username)
	{
		return !who_liked.contains(username);
	}
}

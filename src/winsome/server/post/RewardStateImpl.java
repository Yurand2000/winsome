package winsome.server.post;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RewardStateImpl implements RewardState
{
	private int iteration_count;
	private int total_likes;
	private Set<String> recent_positive_likes;
	private Set<String> recent_commentors;
	private Map<String, CommentCounter> comment_count_per_user;

	public RewardStateImpl()
	{
		iteration_count = 1;
		total_likes = 0;
		recent_positive_likes = new HashSet<String>();
		recent_commentors = new HashSet<String>();
		comment_count_per_user = new HashMap<String, CommentCounter>();
	}
	
	public RewardStateImpl(int iteration_count, int total_likes,
		Set<String> recent_positive_likes, Set<String> recent_commentors,
		Map<String, CommentCounter> comment_count_per_user)
	{
		this.iteration_count = iteration_count;
		this.total_likes = total_likes;
		this.recent_positive_likes = new HashSet<String>(recent_positive_likes);
		this.recent_commentors = new HashSet<String>(recent_commentors);
		this.comment_count_per_user = new HashMap<String, CommentCounter>(comment_count_per_user);
	}
	
	@Override
	public void addLike(String username)
	{
		total_likes++;
		recent_positive_likes.add(username);
	}

	@Override
	public void addDislike()
	{
		total_likes--;
	}

	@Override
	public void addComment(String username)
	{
		recent_commentors.add(username);
		if(comment_count_per_user.containsKey(username))
		{
			comment_count_per_user.get(username).addComment();
		}
		else
		{
			comment_count_per_user.put(username, new CommentCounter(1));
		}
	}

	@Override
	public Reward calcLastReward()
	{
		Reward reward = new Reward(calcTotalReward(), calcContributors());
		
		iteration_count++;
		resetCounters();		
		return reward;
	}
	
	private double calcTotalReward()
	{
		return (calcLikesReward() + calcCommentsReward()) /	iteration_count;
	}
	
	private double calcLikesReward()
	{
		if(total_likes > 0)
			return Math.log(total_likes + 1);
		else
			return 0;
	}
	
	private double calcCommentsReward()
	{
		if(recent_commentors.size() > 0)
		{
			double total = 0;
			for(String commentor : recent_commentors)
			{
				total += 2 / ( 1 + Math.exp(1 - getCommentsMadeByUser(commentor)) );
			}
			
			return Math.log(total + 1);
		}
		else
			return 0;
	}
	
	private Set<String> calcContributors()
	{
		Set<String> contributors = new HashSet<String>();
		contributors.addAll(recent_positive_likes);
		contributors.addAll(recent_commentors);
		return contributors;
	}
	
	private void resetCounters()
	{
		total_likes = 0;
		recent_commentors.clear();
		recent_positive_likes.clear();
	}
	
	private int getCommentsMadeByUser(String username)
	{
		if(comment_count_per_user.containsKey(username))
		{
			return comment_count_per_user.get(username).getCommentCount();
		}
		else
		{
			return 0;
		}
	}
	
	public static class CommentCounter
	{
		private int comment_count;
		
		public CommentCounter(int comment_count)
		{
			this.comment_count = comment_count;
		}
		
		public int getCommentCount()
		{
			return comment_count;
		}
		
		public void addComment()
		{
			comment_count++;
		}
	}
}

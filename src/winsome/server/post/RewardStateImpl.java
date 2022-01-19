package winsome.server.post;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeName("reward_state_impl")
public class RewardStateImpl implements RewardState
{
	@JsonProperty() private int iteration_count;
	@JsonProperty() private int total_likes;
	@JsonProperty() private Set<String> recent_likers;
	@JsonProperty() private Set<String> recent_commentors;
	@JsonProperty() private Map<String, CommentCounter> comment_count_per_user;

	public RewardStateImpl()
	{
		iteration_count = 1;
		total_likes = 0;
		recent_likers = new HashSet<String>();
		recent_commentors = new HashSet<String>();
		comment_count_per_user = new HashMap<String, CommentCounter>();
	}
	
	public RewardStateImpl(int iteration_count, int total_likes,
		Set<String> recent_likers, Set<String> recent_commentors,
		Map<String, CommentCounter> comment_count_per_user)
	{
		this.iteration_count = iteration_count;
		this.total_likes = total_likes;
		this.recent_likers = new HashSet<String>(recent_likers);
		this.recent_commentors = new HashSet<String>(recent_commentors);
		this.comment_count_per_user = new HashMap<String, CommentCounter>(comment_count_per_user);
	}
	
	@Override
	public void addLike(String username)
	{
		total_likes++;
		recent_likers.add(username);
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
	
	@Override
	public RewardStateImpl clone()
	{
		Map<String, CommentCounter> comment_counters = new HashMap<String, CommentCounter>();
		for(Map.Entry<String, CommentCounter> entry : comment_count_per_user.entrySet())
		{
			comment_counters.put(entry.getKey(), entry.getValue().clone());
		}
		return new RewardStateImpl(iteration_count, total_likes, recent_likers, recent_commentors, comment_counters);
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
		contributors.addAll(recent_likers);
		contributors.addAll(recent_commentors);
		return contributors;
	}
	
	private void resetCounters()
	{
		total_likes = 0;
		recent_commentors.clear();
		recent_likers.clear();
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

	@JsonSerialize(using = CommentCounter.CommentCounterSerializer.class)
	@JsonDeserialize(using = CommentCounter.CommentCounterDeserializer.class)
	public static class CommentCounter implements Cloneable
	{
		private int comment_count;
		
		@SuppressWarnings("unused")
		private CommentCounter() { comment_count = 0; }
		
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
		
		@Override
		public CommentCounter clone()
		{
			return new CommentCounter(comment_count);
		}
		
		public static class CommentCounterSerializer extends JsonSerializer<CommentCounter>
		{
			@Override
			public void serialize(CommentCounter value, JsonGenerator gen, SerializerProvider serializers) throws IOException
			{
				gen.writeNumber(value.comment_count);			
			}
		}
		
		public static class CommentCounterDeserializer extends JsonDeserializer<CommentCounter>
		{
			@Override
			public CommentCounter deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
			{
				TreeNode n = p.readValueAsTree();
				if(n.isValueNode())
				{
					return new CommentCounter(Integer.parseInt(n.toString()));
				}
				else
				{
					throw new IOException("Error in comment counter deserialization.");
				}
			}
			
		}
	}
}

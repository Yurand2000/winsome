package winsome.server_app.post;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface RewardState extends Cloneable
{
	void addLike(String username);
	void addDislike();
	void addComment(String username);
	Reward calcLastReward();
	
	RewardState clone();
	
	public static class Reward
	{
		@JsonProperty() public final Double reward;
		@JsonProperty() public final Set<String> contributors;
		
		public Reward(double reward, Set<String> contributors)
		{
			this.reward = reward;
			this.contributors = new HashSet<String>(contributors);
		}
	}
}

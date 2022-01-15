package winsome.server;

import java.util.HashSet;
import java.util.Set;

public interface RewardState
{
	void addLike(String username);
	void addDislike();
	void addComment(String username);
	Reward calcLastReward();
	
	public static class Reward
	{
		public final Double reward;
		public final Set<String> contributors;
		
		public Reward(double reward, Set<String> contributors)
		{
			this.reward = reward;
			this.contributors = new HashSet<String>(contributors);
		}
	}
}

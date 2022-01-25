package winsome.server_app.internal.tasks.wallet_update;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewardState.Reward;

public class CalculatePostRewardTask extends WinsomeTask
{
	private final ConcurrentMap<String, AtomicLong> user_rewards;
	private final BigDecimal author_part;
	private final AtomicInteger total_posts;
	private final GenericPost post;

	public CalculatePostRewardTask(WinsomeData data, ConcurrentMap<String, AtomicLong> user_rewards, BigDecimal author_part, AtomicInteger total_posts, GenericPost post)
	{
		super(data);
		this.user_rewards = user_rewards;
		this.author_part = author_part;
		this.total_posts = total_posts;
		this.post = post;
	}
	
	@Override
	public void run(ServerThreadpool pool)
	{
		Reward reward = post.calculateReward();
		
		Long author_reward = getAuthorReward(reward.reward);
		Long contributor_reward = getContributorReward(reward.reward, reward.contributors.size());
		
		addRewards(reward.contributors, author_reward, contributor_reward);		
		updateOperationCounter(pool);
	}
	
	private Long getAuthorReward(Double total_reward)
	{
		BigDecimal reward = new BigDecimal(total_reward).multiply(author_part);
		return reward.longValue();
	}
	
	private Long getContributorReward(Double total_reward, Integer contributors)
	{
		if(contributors == 0)
		{
			return 0L;
		}
		else
		{
			BigDecimal contibutor_part = new BigDecimal(1).subtract(author_part);
			BigDecimal reward = new BigDecimal(total_reward).multiply(contibutor_part).divide(new BigDecimal(contributors), RoundingMode.FLOOR);
			return reward.longValue();
		}
	}
	
	private void addRewards(Set<String> contributors, Long author_reward, Long contributor_reward)
	{
		addUserReward(post.getAuthor(), author_reward);
		for(String contributor : contributors)
		{
			addUserReward(contributor, contributor_reward);
		}
	}
	
	private void addUserReward(String user, Long amount)
	{
		AtomicLong user_total = user_rewards.get(user);
		if(user_total != null)
			user_total.addAndGet(amount);
	}
	
	private void updateOperationCounter(ServerThreadpool pool)
	{
		if(total_posts.decrementAndGet() == 0)
		{
			pool.enqueueTask(new UpdateWalletsTask(data, user_rewards));
		}
	}
}

package winsome.server_app.internal.tasks.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewardState.Reward;

public class CalculatePostRewardTask implements WinsomeTask
{
	private final ConcurrentMap<String, AtomicLong> user_rewards;
	private final BigDecimal author_part;
	private final AtomicInteger total_posts;
	private final GenericPost post;

	public CalculatePostRewardTask(ConcurrentMap<String, AtomicLong> user_rewards, BigDecimal author_part, AtomicInteger total_posts, GenericPost post)
	{
		this.user_rewards = user_rewards;
		this.author_part = author_part;
		this.total_posts = total_posts;
		this.post = post;
	}
	
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		Reward reward = post.calculateReward();
		
		BigDecimal author_reward = new BigDecimal(reward.reward).multiply(author_part);
		BigDecimal contributor_part = new BigDecimal(reward.reward).subtract(author_reward).divide(new BigDecimal(reward.contributors.size()), RoundingMode.FLOOR);
		
		user_rewards.get(post.getAuthor()).addAndGet(author_reward.longValueExact());
		for(String contributor : reward.contributors)
		{
			user_rewards.get(contributor).addAndGet(contributor_part.longValueExact());
		}
		
		if(total_posts.decrementAndGet() == 0)
		{
			server.executeTask(new UpdateWalletsTask(user_rewards));
		}		
	}
}

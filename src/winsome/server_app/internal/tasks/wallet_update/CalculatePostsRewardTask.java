package winsome.server_app.internal.tasks.wallet_update;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.GenericPost;

public class CalculatePostsRewardTask extends WinsomeTask
{
	public CalculatePostsRewardTask(WinsomeData data)
	{
		super(data);
	}
	
	@Override
	public void run(ServerThreadpool pool)
	{
		BigDecimal author_part = new BigDecimal(0.7);
		
		ConcurrentMap<String, AtomicLong> user_rewards = new ConcurrentHashMap<String, AtomicLong>();
		for(String username : data.getUsers().keySet())
		{
			user_rewards.put(username, new AtomicLong(0));
		}
		
		AtomicInteger total_posts = new AtomicInteger(data.getPosts().size());
		for(Map.Entry<Integer, GenericPost> entry : data.getPosts().entrySet())
		{
			pool.enqueueTask(new CalculatePostRewardTask( data, user_rewards, author_part, total_posts, entry.getValue() ));
		}
	}

}

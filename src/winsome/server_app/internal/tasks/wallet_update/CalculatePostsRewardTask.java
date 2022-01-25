package winsome.server_app.internal.tasks.wallet_update;

import java.math.BigDecimal;
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
	private final String author_part;
	
	public CalculatePostsRewardTask(WinsomeData data, String author_part)
	{
		super(data);
		this.author_part = author_part;
	}
	
	@Override
	public void run(ServerThreadpool pool)
	{
		BigDecimal author_part = new BigDecimal(this.author_part);
		
		ConcurrentMap<String, AtomicLong> user_rewards = new ConcurrentHashMap<String, AtomicLong>();
		buildConcurrentMap(user_rewards);
		
		AtomicInteger total_posts = new AtomicInteger(data.getPosts().size());
		enqueuePostRewardTasks(pool, user_rewards, author_part, total_posts);
	}
	
	private void buildConcurrentMap(ConcurrentMap<String, AtomicLong> user_rewards)
	{
		for(String username : data.getUsers().keySet())
		{
			user_rewards.put(username, new AtomicLong(0));
		}
	}
	
	private void enqueuePostRewardTasks(ServerThreadpool pool, ConcurrentMap<String, AtomicLong> user_rewards, BigDecimal author_part, AtomicInteger total_posts_counter)
	{
		for(GenericPost post : data.getPosts().values())
		{
			pool.enqueueTask( new CalculatePostRewardTask(data, user_rewards, author_part, total_posts_counter, post) );
		}
	}
}

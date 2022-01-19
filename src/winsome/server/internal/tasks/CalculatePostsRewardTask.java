package winsome.server.internal.tasks;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import winsome.server.internal.WinsomeData;
import winsome.server.internal.WinsomeServer;
import winsome.server.internal.WinsomeTask;
import winsome.server.post.GenericPost;

public class CalculatePostsRewardTask implements WinsomeTask
{
	public CalculatePostsRewardTask()
	{
		
	}
	
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		BigDecimal author_part = new BigDecimal(0.7);
		
		ConcurrentMap<String, AtomicLong> user_rewards = new ConcurrentHashMap<String, AtomicLong>();
		for(String username : server_data.users.keySet())
		{
			user_rewards.put(username, new AtomicLong(0));
		}
		
		AtomicInteger total_posts = new AtomicInteger(server_data.posts.size());
		for(Map.Entry<Integer, GenericPost> entry : server_data.posts.entrySet())
		{
			server.executeTask(new CalculatePostRewardTask( user_rewards, author_part, total_posts, entry.getValue() ));
		}
	}

}

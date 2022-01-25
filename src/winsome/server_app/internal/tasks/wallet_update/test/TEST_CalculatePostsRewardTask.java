package winsome.server_app.internal.tasks.wallet_update.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server_app.internal.tasks.wallet_update.CalculatePostRewardTask;
import winsome.server_app.internal.tasks.wallet_update.CalculatePostsRewardTask;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;
import winsome.server_app.post.GenericPost;

class TEST_CalculatePostsRewardTask extends WalletUpdateTest
{
	CalculatePostsRewardTask task;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		task = new CalculatePostsRewardTask(data, "0.7");
		
		data.getUsers().put("user", null);
		data.getUsers().put("user2", null);
		data.getUsers().put("user3", null);
		
		data.getPosts().put(10, new GenericPostTest(10));
		data.getPosts().put(11, new GenericPostTest(11));
		data.getPosts().put(12, new GenericPostTest(12));
		data.getPosts().put(13, new GenericPostTest(13));
	}

	@Test
	void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		assertEquals(pool.enqueued_tasks.size(), 0);
		
		task.run(pool);
		
		assertEquals(pool.enqueued_tasks.size(), 4);
		for(ServerThreadpoolTask task : pool.enqueued_tasks)
		{
			assertTrue(task.getClass() == CalculatePostRewardTask.class);
		}
		checkEnqueuedTask((CalculatePostRewardTask)pool.enqueued_tasks.get(0), 10);
		checkEnqueuedTask((CalculatePostRewardTask)pool.enqueued_tasks.get(1), 11);
		checkEnqueuedTask((CalculatePostRewardTask)pool.enqueued_tasks.get(2), 12);
		checkEnqueuedTask((CalculatePostRewardTask)pool.enqueued_tasks.get(3), 13);
	}
	
	@SuppressWarnings("unchecked")
	void checkEnqueuedTask(CalculatePostRewardTask task, Integer postId) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field user_rewards_field = task.getClass().getDeclaredField("user_rewards");
		user_rewards_field.setAccessible(true);
		Field author_part_field = task.getClass().getDeclaredField("author_part");
		author_part_field.setAccessible(true);
		Field total_posts_field = task.getClass().getDeclaredField("total_posts");
		total_posts_field.setAccessible(true);
		Field post_field = task.getClass().getDeclaredField("post");
		post_field.setAccessible(true);

		ConcurrentMap<String, AtomicLong> user_rewards = (ConcurrentMap<String, AtomicLong>) user_rewards_field.get(task);
		assertTrue( user_rewards.containsKey("user") );
		assertTrue( user_rewards.containsKey("user2") );
		assertTrue( user_rewards.containsKey("user3") );
		assertEquals( user_rewards.get("user").get(), 0L );
		assertEquals( user_rewards.get("user2").get(), 0L );
		assertEquals( user_rewards.get("user3").get(), 0L );
		
		assertEquals( ((BigDecimal)author_part_field.get(task)), new BigDecimal("0.7") );
		assertEquals( ((AtomicInteger)total_posts_field.get(task)).get(), 4 );
		assertEquals( ((GenericPost)post_field.get(task)).postId, postId );
	}
}

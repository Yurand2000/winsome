package winsome.server_app.internal.tasks.wallet_update.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server_app.internal.tasks.wallet_update.CalculatePostRewardTask;
import winsome.server_app.internal.tasks.wallet_update.UpdateWalletsTask;
import winsome.server_app.post.RewardState.Reward;

class TEST_CalculatePostRewardTask extends WalletUpdateTest
{
	CalculatePostRewardTask task;
	ConcurrentMap<String, AtomicLong> user_rewards;
	BigDecimal author_part;
	AtomicInteger total_posts;
	GenericPostTest post;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		user_rewards = new ConcurrentHashMap<String, AtomicLong>();
		user_rewards.put("author", new AtomicLong(0));
		user_rewards.put("user1", new AtomicLong(0));
		user_rewards.put("user2", new AtomicLong(0));
		user_rewards.put("user3", new AtomicLong(0));
		user_rewards.put("user4", new AtomicLong(0));
		
		author_part = new BigDecimal("0.7");
		//can't use new BigDecimal(0.7) because it causes rounding issues.
		total_posts = new AtomicInteger(10);
		post = new GenericPostTest();
		task = new CalculatePostRewardTask(data, user_rewards, author_part, total_posts, post);
	}

	@Test
	void test()
	{
		post.post_calculated_reward = new Reward(100, new HashSet<String>(Arrays.asList(new String[] { "user1", "user2", "user3" })));
		assertFalse(post.calculateReward_called);
		assertFalse(post.getAuthor_called);
		assertEquals(total_posts.get(), 10);
		assertEquals(pool.enqueued_tasks.size(), 0);
		
		task.run(pool);

		assertEquals(pool.enqueued_tasks.size(), 0);
		assertEquals(total_posts.get(), 9);
		assertTrue(post.getAuthor_called);
		assertTrue(post.calculateReward_called);
		
		assertEquals(user_rewards.get("author").get(), 70);
		assertEquals(user_rewards.get("user1").get(), 10);
		assertEquals(user_rewards.get("user2").get(), 10);
		assertEquals(user_rewards.get("user3").get(), 10);
		assertEquals(user_rewards.get("user4").get(), 0);
	}

	@Test
	void testEnqueuesUpdateWalletsTask() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		post.post_calculated_reward = new Reward(100, new HashSet<String>(Arrays.asList(new String[] { "user1", "user2", "user3" })));
		
		total_posts.set(1);
		assertEquals(pool.enqueued_tasks.size(), 0);
		
		task.run(pool);

		assertEquals(pool.enqueued_tasks.size(), 1);
		assertEquals(total_posts.get(), 0);
		
		assertTrue(pool.enqueued_tasks.get(0).getClass() == UpdateWalletsTask.class);		
		checkUpdateWalletTaskArguments((UpdateWalletsTask)pool.enqueued_tasks.get(0));
	}
	
	private void checkUpdateWalletTaskArguments(UpdateWalletsTask task) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field rewards_field = task.getClass().getDeclaredField("rewards");
		rewards_field.setAccessible(true);
		
		@SuppressWarnings("unchecked")
		Map<String, AtomicLong> user_rewards = (Map<String, AtomicLong>) rewards_field.get(task);
		
		assertEquals(user_rewards.get("author").get(), 70);
		assertEquals(user_rewards.get("user1").get(), 10);
		assertEquals(user_rewards.get("user2").get(), 10);
		assertEquals(user_rewards.get("user3").get(), 10);
		assertEquals(user_rewards.get("user4").get(), 0);
	}
}

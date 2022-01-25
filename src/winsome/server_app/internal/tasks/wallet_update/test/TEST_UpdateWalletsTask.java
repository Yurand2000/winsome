package winsome.server_app.internal.tasks.wallet_update.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server_app.internal.tasks.wallet_update.UpdateWalletTask;
import winsome.server_app.internal.tasks.wallet_update.UpdateWalletsTask;
import winsome.server_app.internal.threadpool.ServerThreadpoolTask;

class TEST_UpdateWalletsTask extends WalletUpdateTest
{
	UpdateWalletsTask task;
	Map<String, AtomicLong> rewards;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		rewards = new HashMap<String, AtomicLong>();
		rewards.put("user1", new AtomicLong(50));
		rewards.put("user2", new AtomicLong(10));
		rewards.put("user3", new AtomicLong(0));
		rewards.put("user4", new AtomicLong(196));
		
		task = new UpdateWalletsTask(data, rewards);
	}

	@Test
	void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		assertTrue(pool.enqueued_tasks.isEmpty());
		
		task.run(pool);
		
		assertEquals(pool.enqueued_tasks.size(), 4);
		for(ServerThreadpoolTask task : pool.enqueued_tasks)
		{
			assertTrue(task.getClass() == UpdateWalletTask.class);
		}
		
		checkUpdateWalletTaskArguments((UpdateWalletTask)pool.enqueued_tasks.get(0), "user1", 50L, 4);
		checkUpdateWalletTaskArguments((UpdateWalletTask)pool.enqueued_tasks.get(1), "user2", 10L, 4);
		checkUpdateWalletTaskArguments((UpdateWalletTask)pool.enqueued_tasks.get(2), "user3", 0L, 4);
		checkUpdateWalletTaskArguments((UpdateWalletTask)pool.enqueued_tasks.get(3), "user4", 196L, 4);
	}
	
	private void checkUpdateWalletTaskArguments(UpdateWalletTask task, String username, Long reward, Integer total_wallet_updates) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field username_field = task.getClass().getDeclaredField("username");
		username_field.setAccessible(true);
		Field reward_field = task.getClass().getDeclaredField("reward");
		reward_field.setAccessible(true);
		Field counter_field = task.getClass().getDeclaredField("total_wallet_updates");
		counter_field.setAccessible(true);
		
		assertEquals( username_field.get(task), username );
		assertEquals( reward_field.get(task), reward );
		assertEquals( ((AtomicInteger)counter_field.get(task)).get() , total_wallet_updates );
	}
}

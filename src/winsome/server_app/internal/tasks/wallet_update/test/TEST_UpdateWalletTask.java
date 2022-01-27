package winsome.server_app.internal.tasks.wallet_update.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server_app.internal.tasks.wallet_update.UpdateWalletTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_UpdateWalletTask extends WalletUpdateTest
{
	UpdateWalletTask task;
	AtomicInteger total_wallet_updates;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		total_wallet_updates = new AtomicInteger(10);
		task = new UpdateWalletTask(data, "user", 50L, total_wallet_updates);
		
		data.getUsers().put("user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag") }));
		data.getUsers().put("user2", UserFactory.makeNewUser("user2", "pass", new Tag[]{ new Tag("tag") }));
	}

	@Test
	void test()
	{
		assertEquals(data.getUsers().get("user").getWallet().getCurrentTotal(), 0L);
		assertEquals(data.getUsers().get("user2").getWallet().getCurrentTotal(), 0L);
		assertEquals(total_wallet_updates.get(), 10);
		assertFalse(data.getWalletUpdater().notifyWalletUpdated_called);
		
		task.run(pool);

		assertFalse(data.getWalletUpdater().notifyWalletUpdated_called);
		assertEquals(total_wallet_updates.get(), 9);
		assertEquals(data.getUsers().get("user").getWallet().getCurrentTotal(), 50L);
		assertEquals(data.getUsers().get("user2").getWallet().getCurrentTotal(), 0L);
	}

	@Test
	void testNotifyWalletsUpdated()
	{
		total_wallet_updates.set(1);
		assertEquals(data.getUsers().get("user").getWallet().getCurrentTotal(), 0L);
		assertEquals(data.getUsers().get("user2").getWallet().getCurrentTotal(), 0L);
		assertEquals(total_wallet_updates.get(), 1);
		assertFalse(data.getWalletUpdater().notifyWalletUpdated_called);
		
		task.run(pool);

		assertTrue(data.getWalletUpdater().notifyWalletUpdated_called);
		assertEquals(total_wallet_updates.get(), 0);
		assertEquals(data.getUsers().get("user").getWallet().getCurrentTotal(), 50L);
		assertEquals(data.getUsers().get("user2").getWallet().getCurrentTotal(), 0L);
	}
}

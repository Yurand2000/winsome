package winsome.connection.bridge_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class TEST_WalletNotifier
{
	private String multicast_address;
	private winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater client_updater;
	private winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater server_updater;
	
	@BeforeEach
	void setup() throws UnknownHostException
	{
		multicast_address = "224.0.0.128";
		client_updater = new winsome.connection.client_api.wallet_notifier.WalletNotificationUpdaterImpl();
		server_updater = new winsome.connection.server_api.wallet_notifier.WalletNotificationUpdaterImpl(multicast_address);
	}
	
	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testWalletNotifier() throws IOException, InterruptedException
	{
		AtomicBoolean notified = new AtomicBoolean(false);
		client_updater.registerWalletUpdateNotifications(multicast_address, () -> {
			notified.set(true);
		});
		
		while(!notified.get() && !Thread.currentThread().isInterrupted())
		{
			Thread.sleep(50);
			server_updater.notifyWalletUpdated();
			Thread.yield();
		}
		
		if(Thread.currentThread().isInterrupted())
		{
			fail();
		}
		
		client_updater.unregisterWalletUpdateNotifications();		
	}

}

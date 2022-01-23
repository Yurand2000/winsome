package winsome.connection.bridge_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;

class TEST_WalletNotifier
{
	private String multicast_address;
	private winsome.connection.client_api.wallet_notifier.WalletNotificationUpdaterImpl client_updater;
	
	@BeforeEach
	void setup() throws UnknownHostException
	{
		multicast_address = "224.0.0.128";
		client_updater = new winsome.connection.client_api.wallet_notifier.WalletNotificationUpdaterImpl();
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
			WalletNotificationUpdater.NotifyWalletUpdated(multicast_address);
			Thread.yield();
		}
		
		if(Thread.currentThread().isInterrupted())
		{
			fail();
		}
		
		client_updater.unregisterWalletUpdateNotifications();		
	}

}

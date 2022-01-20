package winsome.connection.bridge_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;

class WalletNotifierTest
{
	private InetAddress multicast_address;
	private winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater client_updater;
	
	@BeforeEach
	void setup() throws UnknownHostException
	{
		multicast_address = InetAddress.getByName("224.0.0.128");
		client_updater = new winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater();
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

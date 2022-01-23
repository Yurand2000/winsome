package winsome.connection.client_api.wallet_notifier.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdaterImpl;
import winsome.connection.protocols.WalletNotification;
import winsome.connection.protocols.WinsomeConnectionProtocol;

class TEST_WalletNotificationUpdaterImpl
{
	private static String multicast_address;

	@Test
	void testThrowsOnInvalidMulticastAddress()
	{
		assertThrows(RuntimeException.class, () ->
		{
			new WalletNotificationUpdaterImpl()
				.registerWalletUpdateNotifications("unknown address", () -> { });
		});
	}
	
	@Test
	@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
	void testNotificationReceived() throws IOException, InterruptedException
	{
		AtomicBoolean notification_received = new AtomicBoolean(false);
		multicast_address = "224.0.0.128";
		
		WalletNotificationUpdaterImpl updater = new WalletNotificationUpdaterImpl();
		updater.registerWalletUpdateNotifications(multicast_address,	
			() -> {
				notification_received.set(true);
			});

		assertFalse(notification_received.get());
		
		byte[] message = WalletNotification.getNotificationMessage();

		while(!notification_received.get() && !Thread.currentThread().isInterrupted())
		{
			Thread.sleep(50);
			sendDatagram(message);
			Thread.yield();
		}
		
		updater.unregisterWalletUpdateNotifications();
	}
	
	void sendDatagram(byte[] message) throws IOException
	{
		DatagramPacket packet = new DatagramPacket(message, message.length);
		packet.setAddress(InetAddress.getByName(multicast_address));
		packet.setPort(WinsomeConnectionProtocol.getUDPMulticastPort());
		DatagramSocket socket = new DatagramSocket();
		socket.send(packet);
		socket.close();
	}
	
}

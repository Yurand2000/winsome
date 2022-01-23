package winsome.connection.server_api.wallet_notifier.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.connection.protocols.WalletNotification;
import winsome.connection.protocols.WinsomeConnectionProtocol;

class TEST_WalletNotificationUpdater
{
	@Test
	void testThrowsOnUnknownAddress()
	{
		assertThrows(RuntimeException.class, () ->
		{
			WalletNotificationUpdater.setMulticastAddress("unknown address");
			WalletNotificationUpdater.notifyWalletUpdated();
		});
	}
	
	@Test
	void test() throws IOException
	{
		byte[] message = new byte[WalletNotification.getNotificationMessage().length];
		DatagramPacket incoming_packet = new DatagramPacket(message, message.length);
		String multicast_address = "224.0.0.128";
		InetAddress address = InetAddress.getByName(multicast_address);
		
		MulticastSocket socket = new MulticastSocket(WinsomeConnectionProtocol.getUDPMulticastPort());
		socket.joinGroup(address);

		WalletNotificationUpdater.setMulticastAddress(multicast_address);
		WalletNotificationUpdater.notifyWalletUpdated();
		
		socket.receive(incoming_packet);
		assertTrue(Arrays.equals(incoming_packet.getData(), WalletNotification.getNotificationMessage()));
		
		socket.leaveGroup(address);
		socket.close();
		socket = null;
	}
}

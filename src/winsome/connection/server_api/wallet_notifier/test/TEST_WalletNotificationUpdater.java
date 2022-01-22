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
	void test() throws IOException
	{
		byte[] message = new byte[WalletNotification.getNotificationMessage().length];
		DatagramPacket incoming_packet = new DatagramPacket(message, message.length);
		InetAddress multicast_address = InetAddress.getByName("224.0.0.128");
		
		MulticastSocket socket = new MulticastSocket(WinsomeConnectionProtocol.getUDPMulticastPort());
		socket.joinGroup(multicast_address);
		
		WalletNotificationUpdater.NotifyWalletUpdated(multicast_address);
		
		socket.receive(incoming_packet);
		assertTrue(Arrays.equals(incoming_packet.getData(), WalletNotification.getNotificationMessage()));
		
		socket.leaveGroup(multicast_address);
		socket.close();
		socket = null;
	}
}

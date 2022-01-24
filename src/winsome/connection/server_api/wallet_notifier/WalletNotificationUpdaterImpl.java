package winsome.connection.server_api.wallet_notifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.connection.protocols.WalletNotification;

public class WalletNotificationUpdaterImpl implements WalletNotificationUpdater
{
	private final String multicast_address;
	
	public WalletNotificationUpdaterImpl(String address)
	{
		multicast_address = address;
	}
	
	public void notifyWalletUpdated()
	{
		try
		{
			InetAddress address = InetAddress.getByName(multicast_address);
			DatagramPacket packet = makeWalletNotificationDatagram(address, WinsomeConnectionProtocol.getUDPMulticastPort());
			sendDatagram(packet);
		}
		catch (IOException e) { throw new RuntimeException(e.toString()); }
	}
	
	public String getMulticastAddress()
	{
		return multicast_address;
	}
	
	private DatagramPacket makeWalletNotificationDatagram(InetAddress multicast_address, int port)
	{
		byte[] message = WalletNotification.getNotificationMessage();
		DatagramPacket packet = new DatagramPacket(message, message.length);
		packet.setAddress(multicast_address);
		packet.setPort(port);
		return packet;
	}
	
	private void sendDatagram(DatagramPacket packet) throws IOException
	{
		DatagramSocket socket = new DatagramSocket();
		socket.send(packet);
		socket.close();
	}
}

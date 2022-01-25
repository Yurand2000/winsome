package winsome.connection.client_api.wallet_notifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import winsome.connection.protocols.WalletNotification;

public class WalletNotificationUpdaterImpl implements WalletNotificationUpdater, Runnable
{
	private InetAddress multicast_address;
	private MulticastSocket socket;
	private Thread notifier_thread;
	private Runnable notification_task;
	private DatagramPacket incoming_packet;
	
	public WalletNotificationUpdaterImpl()
	{
		notifier_thread = null;
		notification_task = null;
		
		byte[] message = new byte[128];
		incoming_packet = new DatagramPacket(message, message.length);
	}

	public void registerWalletUpdateNotifications(String address, Integer port, Runnable task)
	{
		try
		{
			multicast_address = InetAddress.getByName(address);
			if(!multicast_address.isMulticastAddress())
			{
				throw new RuntimeException("given address " + address  + " is not a multicast address");
			}
			notification_task = task;
			socket = new MulticastSocket(port);
			notifier_thread = new Thread(this);
			notifier_thread.start();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.toString());
		}
	}
	
	public void unregisterWalletUpdateNotifications()
	{
		try
		{
			socket.close();
			notifier_thread.interrupt();
			notifier_thread.join();
			notifier_thread = null;
			socket = null;
		}
		catch (InterruptedException e) { }
	}
	
	@Override
	public void run()
	{
		try 
		{
			setupUdpSocket();
			
			while(!Thread.currentThread().isInterrupted())
			{
				waitForPacket();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void setupUdpSocket() throws IOException
	{
		socket.joinGroup(multicast_address);
	}
	
	private void waitForPacket()
	{
		try { socket.receive(incoming_packet); }
		catch(IOException e) { return; }
		
		if(incomingPacketIsWalletNotification())
		{
			notification_task.run();
		}
	}
	
	private boolean incomingPacketIsWalletNotification()
	{
		if(incoming_packet.getLength() != WalletNotification.getNotificationMessage().length)
		{
			return false;
		}
		
		byte[] incoming = incoming_packet.getData();
		byte[] expected = WalletNotification.getNotificationMessage();
		for(int i = 0; i < incoming_packet.getLength(); i++)
		{
			if(incoming[i] != expected[i])
				return false;
		}
		return true;
	}
}

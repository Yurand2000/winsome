package winsome.connection.client_api.wallet_notifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import winsome.connection.protocols.WinsomeConnectionProtocol;
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

	public void registerWalletUpdateNotifications(String address, Runnable task)
	{
		try
		{
			multicast_address = InetAddress.getByName(address);
			notification_task = task;
			notifier_thread = new Thread(this);
			notifier_thread.start();
		}
		catch (UnknownHostException e)
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
		socket = new MulticastSocket(WinsomeConnectionProtocol.getUDPMulticastPort());
		socket.joinGroup(multicast_address);
	}
	
	private void waitForPacket()
	{
		try { socket.receive(incoming_packet); }
		catch(IOException e) { return; }
		
		if(checkIncomingPacket())
		{
			notification_task.run();
		}
	}
	
	private boolean checkIncomingPacket()
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

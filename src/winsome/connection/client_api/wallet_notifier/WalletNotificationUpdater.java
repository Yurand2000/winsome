package winsome.connection.client_api.wallet_notifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.connection.protocols.WalletNotification;

public class WalletNotificationUpdater implements Runnable
{
	private InetAddress multicast_address;
	private MulticastSocket socket;
	private Thread notifier_thread;
	private Runnable notification_task;
	private DatagramPacket incoming_packet;
	
	public WalletNotificationUpdater()
	{
		notifier_thread = null;
		notification_task = null;
		
		byte[] message = new byte[128];
		incoming_packet = new DatagramPacket(message, message.length);
	}

	public void registerWalletUpdateNotifications(InetAddress address, Runnable task)
	{
		multicast_address = address;
		notification_task = task;
		notifier_thread = new Thread(this);
		notifier_thread.start();
	}
	
	public void unregisterWalletUpdateNotifications() throws InterruptedException
	{
		notifier_thread.interrupt();
		notifier_thread.join();
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
			
			teardownUdpSocket();
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
	
	private void waitForPacket() throws IOException
	{
		socket.receive(incoming_packet);
		
		if(Arrays.equals(incoming_packet.getData(), WalletNotification.getNotificationMessage()))
		{
			notification_task.run();
		}
	}
	
	private void teardownUdpSocket() throws IOException
	{
		socket.leaveGroup(multicast_address);
		socket.close();
		socket = null;
	}
}

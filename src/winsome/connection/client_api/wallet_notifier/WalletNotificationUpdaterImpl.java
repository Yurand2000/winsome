package winsome.connection.client_api.wallet_notifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import winsome.connection.protocols.WalletNotification;

public class WalletNotificationUpdaterImpl implements WalletNotificationUpdater, Runnable
{
	private InetAddress address;
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
			tryRegisterForUpdates(address, port, task);
		}
		catch (IOException e)
		{
			unregisterWalletUpdateNotifications();
			throw new RuntimeException(e.toString());
		}
	}
	
	private void tryRegisterForUpdates(String address_name, Integer port, Runnable task) throws IOException
	{
		address = InetAddress.getByName(address_name);
		checkMulticastAddress(address_name, address);
		notification_task = task;
		
		socket = new MulticastSocket(port);
		notifier_thread = new Thread(this);
		notifier_thread.start();
	}
	
	private void checkMulticastAddress(String address_name, InetAddress address)
	{
		if(address == null || !address.isMulticastAddress())
		{
			throw new RuntimeException("given address " + address_name + " is not a multicast address");
		}
	}
	
	public void unregisterWalletUpdateNotifications()
	{
		closeSocket();
		stopNotifierThread();
		notification_task = null;
		address = null;
	}
	
	private void closeSocket()
	{
		if(socket != null)
		{
			socket.close();
			socket = null;
		}
	}
	
	private void stopNotifierThread()
	{
		if(notifier_thread != null)
		{
			try
			{
				notifier_thread.interrupt();
				notifier_thread.join();
			}
			catch (InterruptedException e) { }
			finally
			{
				notifier_thread = null;
			}
		}
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
		socket.joinGroup(address);
	}
	
	private void waitForPacket()
	{
		try { socket.receive(incoming_packet); }
		catch(IOException e) { return; }
		catch(NullPointerException e) { return; }
		
		if(incomingPacketIsWalletNotification())
		{
			notification_task.run();
		}
	}
	
	private boolean incomingPacketIsWalletNotification()
	{		
		byte[] incoming = Arrays.copyOf(incoming_packet.getData(), incoming_packet.getLength());
		byte[] expected = WalletNotification.getNotificationMessage();
		return Arrays.equals(incoming, expected);
	}
}

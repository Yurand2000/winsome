package winsome.server_app.internal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import winsome.connection.server_api.socket.SocketInformations;
import winsome.server_app.internal.tasks.impl.socket.Task_SocketRead;
import winsome.server_app.internal.tasks.impl.socket.Task_SocketWrite;

public class ClientHandler implements Runnable
{
	private final InetSocketAddress address;
	private final WinsomeServer server;

	private ServerSocketChannel listening_socket;
	private Selector selector;
	private Thread selector_thread;
	private boolean is_paused;
	private boolean is_stopped;
	
	public ClientHandler(InetSocketAddress address, WinsomeServer server)
	{
		this.address = address;
		this.server = server;

		listening_socket = null;
		selector = null;
		selector_thread = null;
		is_paused = false;
		is_stopped = false;
	}
	
	public void startClientHandler() throws IOException
	{
		selector = Selector.open();
		listening_socket = ServerSocketChannel.open();
		listening_socket.socket().bind(address);
		listening_socket.configureBlocking(false);		
		listening_socket.register(selector, SelectionKey.OP_ACCEPT);
		
		selector_thread = new Thread(this);
		selector_thread.start();
	}
	
	public void stopClientHandler() throws InterruptedException, IOException
	{
		is_stopped = true;
		listening_socket.close();
		selector.close();
		selector_thread.interrupt();
		synchronized(selector_thread)
			{ selector_thread.notify(); }
		selector_thread.join();
		
		listening_socket = null;
		selector = null;
		selector_thread = null;
		is_paused = false;
		is_stopped = false;
	}
	
	public void pauseClientHandler()
	{
		is_paused = true;
	}
	
	public void resumeClientHandler()
	{
		is_paused = false;
		selector_thread.notify();
	}

	@Override
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			waitIfPaused();
			
			if(!is_stopped)
			{
				tryRunClientHandler();
			}
		}
	}
	
	private void waitIfPaused()
	{
		while(is_paused && !is_stopped)
		{
			try
			{ 
				synchronized(selector_thread)
					{ selector_thread.wait(); }
			}
			catch (InterruptedException e) { }
		}
	}
	
	private void tryRunClientHandler()
	{
		try
		{
			runClientHandler();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void runClientHandler() throws IOException
	{
		int ready_keys = selector.select();
		if(ready_keys != 0)
		{
			Iterator<SelectionKey> keys_iterator = selector.selectedKeys().iterator();
			while(keys_iterator.hasNext())
			{
				executeIterator(keys_iterator.next());
				keys_iterator.remove();
			}
		}
	}
	
	private void executeIterator(SelectionKey key)
	{
		try
		{
			if(key.isAcceptable())
			{
				executeAcceptableKey(key);
			}
			else
			{
				executeReadWriteKey(key);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error on handling client request: " + e.getMessage());
		}
	}
	
	private void executeAcceptableKey(SelectionKey key) throws IOException
	{
		SocketChannel new_channel = listening_socket.accept();
		new_channel.configureBlocking(false);
		
		SelectionKey new_key = new_channel.register(selector, SelectionKey.OP_READ);
		new_key.attach(new SocketInformations(new_key));
	}
	
	private void executeReadWriteKey(SelectionKey key)
	{
		key.interestOps(0);		
		if(key.isReadable())
		{
			server.executeTask(new Task_SocketRead(key));
		}
		
		if(key.isWritable())
		{
			server.executeTask(new Task_SocketWrite(key));
		}
	}
}

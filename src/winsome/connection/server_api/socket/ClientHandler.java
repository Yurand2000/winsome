package winsome.connection.server_api.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import winsome.connection.server_api.socket.tasks.SocketReadTask;
import winsome.connection.server_api.socket.tasks.SocketWriteTask;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.pausable_threads.PausableThread;
import winsome.server_app.internal.pausable_threads.PausableThreadMonitor;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class ClientHandler implements Runnable
{
	private final InetSocketAddress address;
	private final ServerThreadpool threadpool;
	private final WinsomeData winsome_data;

	private ServerSocketChannel listening_socket;
	private Selector selector;
	private PausableThreadMonitor selector_thread_monitor;
	private PausableThread selector_thread;
	
	public ClientHandler(InetSocketAddress address, WinsomeData data, ServerThreadpool pool)
	{
		this.address = address;
		
		threadpool = pool;
		winsome_data = data;

		listening_socket = null;
		selector = null;
		selector_thread = null;
		selector_thread_monitor = null;
	}
	
	public void startClientHandler() throws IOException
	{
		selector = Selector.open();
		listening_socket = ServerSocketChannel.open();
		listening_socket.socket().bind(address);
		listening_socket.configureBlocking(false);		
		listening_socket.register(selector, SelectionKey.OP_ACCEPT);
		
		selector_thread_monitor = new PausableThreadMonitor();
		selector_thread = new PausableThread(selector_thread_monitor, this);
		selector_thread.start();
	}
	
	public void stopClientHandler() throws InterruptedException, IOException
	{
		if(selector != null)
		{
			listening_socket.close();
			selector_thread.interrupt();
			selector_thread.join();
			selector.close();
			
			listening_socket = null;
			selector = null;
			selector_thread = null;
			selector_thread_monitor = null;
		}
	}
	
	public void pauseClientHandler()
	{
		selector_thread_monitor.pauseAllThreads();
	}
	
	public void resumeClientHandler()
	{
		selector_thread_monitor.resumeAllThreads();
	}

	@Override
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			tryRunClientHandler();
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
		new_key.attach(new SocketStateImpl(new_key));
	}
	
	private void executeReadWriteKey(SelectionKey key)
	{
		key.interestOps(0);
		SocketState socket = (SocketState) key.attachment();
		
		if(key.isReadable())
		{
			threadpool.enqueueTask(new SocketReadTask(socket, winsome_data));
		}
		else if(key.isWritable())
		{
			threadpool.enqueueTask(new SocketWriteTask(socket, winsome_data));
		}
	}
}

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
import winsome.server_app.internal.threadpool.PausableRunnableMonitor;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class ClientHandler implements Runnable
{
	private final InetSocketAddress address;
	private final ServerThreadpool threadpool;
	private final WinsomeData winsome_data;
	private final SocketStateCommon common_state;

	private ServerSocketChannel listening_socket;
	private Selector selector;
	private PausableRunnableMonitor pausable_runnable_monitor;
	private Thread selector_thread;
	
	public ClientHandler(InetSocketAddress address, WinsomeData data, ServerThreadpool pool)
	{
		this.address = address;
		
		threadpool = pool;
		winsome_data = data;
		common_state = new SocketStateCommonImpl();

		listening_socket = null;
		selector = null;
		selector_thread = null;
		pausable_runnable_monitor = null;
	}
	
	public void startClientHandler() throws IOException
	{
		setupSelectorAndAcceptorSocket();
		setupSelectorThread();
	}
	
	private void setupSelectorAndAcceptorSocket() throws IOException
	{
		selector = Selector.open();
		listening_socket = ServerSocketChannel.open();
		listening_socket.socket().bind(address);
		listening_socket.configureBlocking(false);
		listening_socket.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	private void setupSelectorThread()
	{
		pausable_runnable_monitor = new PausableRunnableMonitor();
		selector_thread = new Thread(pausable_runnable_monitor.makePausableRunnable(this));
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
			pausable_runnable_monitor = null;
		}
	}
	
	public void pauseClientHandler()
	{
		pausable_runnable_monitor.pauseAllThreads();
	}
	
	public void resumeClientHandler()
	{
		pausable_runnable_monitor.resumeAllThreads();
	}

	@Override
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			runClientHandler();
		}
	}
	
	private void runClientHandler()
	{
		try
		{
			tryRunClientHandler();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void tryRunClientHandler() throws IOException
	{
		if(selector.select() != 0)
		{
			Iterator<SelectionKey> selected_keys = selector.selectedKeys().iterator();
			while(selected_keys.hasNext())
			{
				handleChannel(selected_keys.next());
				selected_keys.remove();
			}
		}
	}
	
	private void handleChannel(SelectionKey key)
	{
		try
		{
			if(key.isAcceptable())
			{
				handleAcceptableChannel(key);
			}
			else
			{
				handleReadWriteChannel(key);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error on handling client request: " + e.getMessage());
		}
	}
	
	private void handleAcceptableChannel(SelectionKey key) throws IOException
	{
		SocketChannel new_channel = listening_socket.accept();
		configureNewChannel(new_channel);
	}
	
	private void configureNewChannel(SocketChannel new_channel) throws IOException
	{
		new_channel.configureBlocking(false);		
		SelectionKey new_key = new_channel.register(selector, SelectionKey.OP_READ);
		new_key.attach( new SocketStateImpl(new_key, common_state) );
	}
	
	private void handleReadWriteChannel(SelectionKey key)
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

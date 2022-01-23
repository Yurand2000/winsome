package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.socket.ClientHandler;
import winsome.connection.server_api.socket.tasks.SocketReadTask;
import winsome.connection.server_api.socket.tasks.SocketWriteTask;
import winsome.connection.socket_messages.Message;
import winsome.generic.SerializerWrapper;

class TEST_ClientHandler
{
	private WinsomeDataTest data = null;
	private ServerThreadpoolTest pool = null;
	private SocketChannel client_channel;
	private ClientHandler handler;
	private InetSocketAddress address;
	
	@BeforeEach
	void setup() throws IOException
	{
		address = new InetSocketAddress(InetAddress.getLocalHost(), 8080);
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		handler = new ClientHandler(address, data, pool);
		handler.startClientHandler();
		
		client_channel = SocketChannel.open();
	}

	@Test
	void testConnect() throws IOException
	{
		client_channel.connect(address);
		
		assertTrue(client_channel.isConnected());
	}
	
	@Test
	void testNewChannelIsSetAsReadable() throws IOException, InterruptedException
	{
		client_channel.connect(address);
		
		assertTrue(client_channel.isConnected());
		
		ByteBuffer buf = ByteBuffer.allocate(64);
		buf.putInt(4);
		buf.put("read".getBytes());
		buf.flip();
		
		client_channel.write(buf);

		pool.waitExecuteTaskCalled();
		pool.checkExpectedTask(SocketReadTask.class);
	}
	
	@Test
	void testNewChannelWriteOperation() throws IOException, InterruptedException
	{
		pool.setWriteTest();
		client_channel.connect(address);
		
		assertTrue(client_channel.isConnected());
		
		ByteBuffer buf = ByteBuffer.allocate(64);
		buf.putInt(4);
		buf.put("read".getBytes());
		buf.flip();
		
		client_channel.write(buf);
		pool.waitExecuteTaskCalled();
		pool.checkExpectedTask(SocketReadTask.class);
		
		buf.clear();
		client_channel.read(buf);
		buf.flip();
		int size = buf.getInt();
		byte[] data = new byte[size];
		buf.get(data);
		
		assertDoesNotThrow(() -> SerializerWrapper.deserialize(data, Message.class));
		pool.checkExpectedTask(SocketWriteTask.class);
	}

	@AfterEach
	void teardown() throws IOException, InterruptedException
	{
		client_channel.close();
		handler.stopClientHandler();
	}
}

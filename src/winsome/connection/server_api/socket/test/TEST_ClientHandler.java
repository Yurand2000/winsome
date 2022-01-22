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

class TEST_ClientHandler
{
	private WinsomeServerTest server;
	private SocketChannel other_channel;
	private ClientHandler handler;
	private InetSocketAddress address;
	
	@BeforeEach
	void setup() throws IOException
	{
		address = new InetSocketAddress(InetAddress.getLocalHost(), 8080);
		server = new WinsomeServerTest();
		handler = new ClientHandler(address, server);
		handler.startClientHandler();
		
		other_channel = SocketChannel.open();
	}

	@Test
	void testConnect() throws IOException
	{
		other_channel.connect(address);
		
		assertTrue(other_channel.isConnected());
	}
	
	@Test
	void testNewChannelIsSetAsReadable() throws IOException, InterruptedException
	{
		other_channel.connect(address);
		
		assertTrue(other_channel.isConnected());
		
		ByteBuffer buf = ByteBuffer.allocate(64);
		buf.putInt(4);
		buf.put("read".getBytes());
		buf.flip();
		
		other_channel.write(buf);

		server.waitExecuteTaskCalled();
		server.checkExpectedTask(SocketReadTask.class);
	}
	
	@Test
	void testNewChannelWriteOperation() throws IOException, InterruptedException
	{
		server.setWriteTest();
		other_channel.connect(address);
		
		assertTrue(other_channel.isConnected());
		
		ByteBuffer buf = ByteBuffer.allocate(64);
		buf.putInt(4);
		buf.put("read".getBytes());
		buf.flip();
		
		other_channel.write(buf);
		server.waitExecuteTaskCalled();
		server.checkExpectedTask(SocketReadTask.class);
		
		buf.clear();
		other_channel.read(buf);
		buf.flip();
		int size = buf.getInt();
		byte[] data = new byte[size];
		buf.get(data);
		
		assertArrayEquals(data, "write".getBytes());
		server.checkExpectedTask(SocketWriteTask.class);
	}

	@AfterEach
	void teardown() throws IOException, InterruptedException
	{
		other_channel.close();
		handler.stopClientHandler();
	}
}

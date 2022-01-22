package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.socket.SocketWriterImpl;

class TEST_SocketWriter
{
	private SelectorTest selector;
	private SelectionKeyTest key;
	private SocketChannel key_channel;
	private SocketChannel other_channel;
	private SocketWriterImpl writer;
	
	@BeforeEach
	void setup() throws IOException
	{
		InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 8080);
		selector = new SelectorTest();
		key_channel = SocketChannel.open();
		key_channel.configureBlocking(false);
		
		ServerSocketChannel acceptor = ServerSocketChannel.open();
		acceptor.bind(address);
		key_channel.connect(address);
		other_channel = acceptor.accept();
		acceptor.close();
		
		key_channel.finishConnect();
		
		key = new SelectionKeyTest(selector, key_channel);
		writer = new SocketWriterImpl(key);
	}

	@Test
	void testExecuteWriteOperation() throws IOException
	{
		byte[] data = "ciao".getBytes();
		writer.addMessageToSend(data);

		assertEquals(key.interestOps(), 0);
		
		writer.executeWriteOperation();

		assertTrue(key.isReadable());
		selector.checkWakeupInvoked();
		
		ByteBuffer buf = ByteBuffer.allocate(16);
		other_channel.read(buf);
		
		assertTrue(buf.position() >= "ciao".getBytes().length + Integer.BYTES);
		buf.flip();
		int size = buf.getInt();
		assertEquals(size, "ciao".getBytes().length);
		data = new byte[size];
		buf.get(data);
		assertArrayEquals(data, "ciao".getBytes());
	}

	@Test
	void testExecuteTwoWriteOperation() throws IOException
	{
		byte[] data = "ciao".getBytes();
		writer.addMessageToSend(data);
		data = "Nicola".getBytes();
		writer.addMessageToSend(data);

		assertEquals(key.interestOps(), 0);
		
		writer.executeWriteOperation();

		assertTrue(key.isReadable());
		selector.checkWakeupInvoked();
		
		ByteBuffer buf = ByteBuffer.allocate(32);
		other_channel.read(buf);
		
		assertTrue(buf.position() >= "ciao".getBytes().length + Integer.BYTES);
		buf.flip();
		int size = buf.getInt();
		assertEquals(size, "ciao".getBytes().length);
		data = new byte[size];
		buf.get(data);
		assertArrayEquals(data, "ciao".getBytes());
		buf.compact();
		
		assertTrue(buf.position() >= "Nicola".getBytes().length + Integer.BYTES);
		buf.flip();
		size = buf.getInt();
		assertEquals(size, "Nicola".getBytes().length);
		data = new byte[size];
		buf.get(data);
		assertArrayEquals(data, "Nicola".getBytes());
	}

	@AfterEach
	void teardown() throws IOException
	{
		key_channel.close();
		other_channel.close();
	}
}

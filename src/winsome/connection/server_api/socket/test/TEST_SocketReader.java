package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.socket.SocketReaderImpl;

class TEST_SocketReader
{
	private SelectorTest selector;
	private SelectionKeyTest key;
	private SocketChannel key_channel;
	private SocketChannel other_channel;
	private SocketReaderImpl reader;
	
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
		reader = new SocketReaderImpl(key);
	}
	
	@Test
	void testNothingReadSetsKeyToReadAndWakesUpSelector()
	{
		assertEquals(key.interestOps(), 0);
		
		reader.executeReadOperation();

		assertFalse(reader.hasMessageBeenRetrived());
		assertEquals(key.interestOps(), SelectionKey.OP_READ);
		selector.checkWakeupInvoked();
	}
	
	@Test
	void testNotEnoughToReadSetsKeyToReadAndWakesUpSelector() throws IOException
	{
		ByteBuffer buf = ByteBuffer.allocate(512);
		buf.putInt(4);
		buf.putChar('c');
		buf.flip();
		other_channel.write(buf);

		assertEquals(key.interestOps(), 0);
		
		reader.executeReadOperation();

		assertFalse(reader.hasMessageBeenRetrived());
		assertEquals(key.interestOps(), SelectionKey.OP_READ);
		selector.checkWakeupInvoked();
		
	}
	
	@Test
	void testEnoughToRead() throws IOException
	{
		ByteBuffer buf = ByteBuffer.allocate(512);
		buf.putInt(4);
		buf.put("ciao".getBytes());
		buf.flip();
		other_channel.write(buf);

		assertEquals(key.interestOps(), 0);
		
		reader.executeReadOperation();

		assertTrue(reader.hasMessageBeenRetrived());
		assertEquals(key.interestOps(), 0);
		
		byte[] msg = reader.getRetrivedMessage();
		assertEquals("ciao", new String(msg));
	}
	
	@Test
	void testMoreThanEnoughToRead() throws IOException
	{
		ByteBuffer buf = ByteBuffer.allocate(512);
		buf.putInt(4);
		buf.put("ciao".getBytes());
		buf.putInt(16);
		buf.flip();
		other_channel.write(buf);

		assertEquals(key.interestOps(), 0);
		
		reader.executeReadOperation();

		assertTrue(reader.hasMessageBeenRetrived());
		assertEquals(key.interestOps(), 0);
		
		byte[] msg = reader.getRetrivedMessage();
		assertFalse(reader.hasMessageBeenRetrived());
		assertEquals("ciao", new String(msg));
	}
	
	@Test
	void testTwoConsecutiveReads() throws IOException
	{
		ByteBuffer buf = ByteBuffer.allocate(512);
		buf.putInt(4);
		buf.put("ciao".getBytes());
		buf.putInt(7);
		buf.put("amicone".getBytes());
		buf.flip();
		other_channel.write(buf);
		
		reader.executeReadOperation();
		assertTrue(reader.hasMessageBeenRetrived());
		
		byte[] msg = reader.getRetrivedMessage();
		assertFalse(reader.hasMessageBeenRetrived());
		assertEquals("ciao", new String(msg));
				
		reader.executeReadOperation();
		assertTrue(reader.hasMessageBeenRetrived());
		msg = reader.getRetrivedMessage();
		assertFalse(reader.hasMessageBeenRetrived());
		assertEquals("amicone", new String(msg));
	}

	@AfterEach
	void teardown() throws IOException
	{
		key_channel.close();
		other_channel.close();
	}
}

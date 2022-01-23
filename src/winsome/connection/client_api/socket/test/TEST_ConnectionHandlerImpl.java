package winsome.connection.client_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import winsome.client_app.api.APIException;
import winsome.connection.client_api.socket.ConnectionHandlerImpl;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.generic.SerializerWrapper;

class TEST_ConnectionHandlerImpl
{
	private InetSocketAddress address;
	private ConnectionHandlerImpl handler;
	private ServerSocketChannel acceptor;
	private Thread acceptor_thread;
	private SocketChannel connection = null;
	
	@BeforeEach
	void setup() throws IOException
	{
		SerializerWrapper.addDeserializer(MessageTest.class);
		SerializerWrapper.addDeserializer(RequestExceptionAnswer.class);
		address = new InetSocketAddress( InetAddress.getLocalHost(), 8080 );
		acceptor = ServerSocketChannel.open();
		acceptor.bind(address);
		acceptor_thread = new Thread(() ->
		{
			try
			{
				connection = acceptor.accept();
				acceptor.close();
			}
			catch(Exception e) { }
		});
		acceptor_thread.start();
		
		handler = new ConnectionHandlerImpl(address);
	}
	
	@Test
	@SuppressWarnings("unused")
	void testConstructor()
	{		
		assertDoesNotThrow(() -> {
			ConnectionHandlerImpl handler = new ConnectionHandlerImpl(address);
		});
	}

	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testConnect() throws IOException, InterruptedException
	{
		handler.connect();
		
		acceptor_thread.join();
		assertTrue(connection != null);
		assertTrue(connection.isConnected());
	}
	
	@Test
	void testConnectThrowsIfCannotConnect() throws IOException, InterruptedException
	{
		acceptor.close();
		acceptor_thread.join();
		
		assertThrows(IOException.class, () -> {
			handler.connect();
		});
	}
	
	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testDisconnect() throws IOException, InterruptedException
	{
		handler.connect();

		acceptor_thread.join();
		assertTrue(connection != null);
		assertTrue(connection.isConnected());
		
		handler.disconnect();
		
		ByteBuffer buffer = ByteBuffer.allocate(16);
		assertEquals(connection.read(buffer), -1);
	}
	
	@Test
	@Timeout(value= 1000, unit= TimeUnit.MILLISECONDS)
	void testReadMessage() throws IOException, InterruptedException
	{
		handler.connect();

		acceptor_thread.join();
		assertTrue(connection != null);
		assertTrue(connection.isConnected());
		
		Thread t = new Thread(() ->
		{
			try
			{
				MessageTest msg = handler.readMessage(MessageTest.class);
				assertEquals(msg.message, "test");
			}
			catch (IOException e) { fail(); }
		});
		t.start();
		
		ByteBuffer buf = prepareMessageToWrite(new MessageTest("test"));
		connection.write(buf);
		
		t.join();
		
		handler.disconnect();
	}
	
	@Test
	void testReadMessageThrowsIfGetsARequestExceptionAnswer() throws IOException, InterruptedException
	{
		handler.connect();

		acceptor_thread.join();
		assertTrue(connection != null);
		assertTrue(connection.isConnected());
		
		Thread t = new Thread(() ->
		{
			assertThrows(APIException.class, () ->
			{
				handler.readMessage(MessageTest.class);
			});
		});
		t.start();
		
		ByteBuffer buf = prepareMessageToWrite(new RequestExceptionAnswer("error"));
		connection.write(buf);
		
		t.join();
		
		handler.disconnect();
	}
	
	private ByteBuffer prepareMessageToWrite(Message msg) throws IOException
	{
		byte[] data = SerializerWrapper.serialize(msg);
		
		ByteBuffer buf = ByteBuffer.allocate(data.length + Integer.BYTES);
		buf.putInt(data.length);
		buf.put(data);
		buf.flip();
		
		return buf;
	}
	
	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testWriteMessage() throws IOException, InterruptedException
	{
		handler.connect();

		acceptor_thread.join();
		assertTrue(connection != null);
		assertTrue(connection.isConnected());
		
		Thread t = new Thread(() ->
		{
			try
			{
				ByteBuffer buf = ByteBuffer.allocate(128);
				connection.read(buf);
				assertTrue(buf.position() > Integer.BYTES);
				buf.flip();
				int size = buf.getInt();
				byte[] data = new byte[size];
				buf.get(data);
				
				MessageTest msg = SerializerWrapper.deserialize(data, MessageTest.class);
				assertEquals(msg.message, "ciao");
			}
			catch (IOException e) { fail(); }
		});
		t.start();

		handler.sendMessage(new MessageTest());
		
		t.join();
		
		handler.disconnect();
	}
	
	@AfterEach
	void teardown() throws IOException
	{
		if(connection != null)
			connection.close();
		acceptor.close();
		handler.disconnect();
	}
}

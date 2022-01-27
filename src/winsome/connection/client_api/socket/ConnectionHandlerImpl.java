package winsome.connection.client_api.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.generic.SerializerWrapper;

public class ConnectionHandlerImpl implements ConnectionHandler
{
	private final InetSocketAddress server_address;
	private SocketChannel socket;
	private final ByteBuffer read_buffer;
	private final ByteBuffer write_buffer;
	
	public ConnectionHandlerImpl(InetSocketAddress server_address)
	{
		this.server_address = server_address;
		this.read_buffer = ByteBuffer.allocate(512);
		this.write_buffer = ByteBuffer.allocate(512);
		this.socket = null;
	}
	
	public void connect() throws IOException
	{
		socket = SocketChannel.open();	
		socket.connect(server_address);
	}
	
	public <T extends Message> T readMessage(Class<T> type) throws IOException
	{
		Message message = readMessage();
		
		if(message.getClass() == type)
		{
			return type.cast(message);
		}
		else if(message.getClass() == RequestExceptionAnswer.class)
		{
			throw RequestExceptionAnswer.class.cast(message).makeException();
		}
		else
		{
			throw new ServerInternalException("Could not deserialize message.");
		}
	}
	
	private Message readMessage() throws IOException
	{
		int length = readMessageLength();
		byte[] data = readMessage(length);
		return SerializerWrapper.deserialize(data, Message.class);
	}
	
	public <T extends Message> void sendMessage(T message) throws IOException
	{
		byte[] data = SerializerWrapper.serializeCompact(message);
		write_buffer.putInt(data.length);
		write_buffer.put(data);
		
		while(write_buffer.position() > 0)
		{
			write_buffer.flip();
			socket.write(write_buffer);
			write_buffer.compact();
		}
	}
	
	public void disconnect()
	{
		try
		{
			if(socket != null)
			{
				socket.close();
			}
		}
		catch (IOException e) { }
		finally
		{
			socket = null;
		}
	}
	
	private int readMessageLength() throws IOException
	{
		while(!hasReadAtLeastBytes(Integer.BYTES))
		{
			tryReadingFromSocket();
		}
		
		read_buffer.flip();
		int dataSize = read_buffer.getInt();
		read_buffer.compact();
		return dataSize;
	}
	
	private byte[] readMessage(int length) throws IOException
	{
		while(!hasReadAtLeastBytes(length))
		{
			tryReadingFromSocket();
		}

		byte[] data = new byte[length];
		read_buffer.flip();
		read_buffer.get(data);
		read_buffer.compact();
		return data;
	}

	private void tryReadingFromSocket() throws IOException
	{
		socket.read(read_buffer);
	}
	
	private boolean hasReadAtLeastBytes(int bytes) throws IOException
	{
		return read_buffer.position() >= bytes;
	}
}

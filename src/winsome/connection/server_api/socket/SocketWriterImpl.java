package winsome.connection.server_api.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SocketWriterImpl implements SocketWriter
{
	private final SelectionKey key;
	private final ByteBuffer write_buffer;
	
	public SocketWriterImpl(SelectionKey key)
	{
		this.key = key;
		this.write_buffer = ByteBuffer.allocate(512);		
	}
	
	@Override
	public void executeWriteOperation() throws IOException
	{
		try 
		{
			tryExecuteWriteOperation();
		}
		catch (IOException e)
		{
			SocketUtils.destroyKey(key);
			throw e;
		}
	}
	
	private void tryExecuteWriteOperation() throws IOException
	{
		write_buffer.flip();
		((SocketChannel)key.channel()).write(write_buffer);
		
		if(write_buffer.hasRemaining())
		{
			write_buffer.compact();
			SocketUtils.setSocketReadyToWrite(key);
		}
		else
		{
			write_buffer.clear();
			SocketUtils.setSocketReadyToRead(key);
		}
	}

	@Override
	public void addMessageToSend(byte[] data)
	{
		write_buffer.putInt(data.length);
		write_buffer.put(data);
	}
}

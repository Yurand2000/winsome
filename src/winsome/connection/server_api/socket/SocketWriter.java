package winsome.connection.server_api.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import winsome.server_app.internal.tasks.TaskUtils;

public class SocketWriter
{
	private final SelectionKey key;
	private final ByteBuffer write_buffer;
	
	public SocketWriter(SelectionKey key)
	{
		this.key = key;
		this.write_buffer = ByteBuffer.allocate(512);		
	}
	
	public void executeWriteOperation()
	{
		try 
		{
			tryExecuteWriteOperation();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void tryExecuteWriteOperation() throws IOException
	{
		write_buffer.flip();
		((SocketChannel)key.channel()).write(write_buffer);
		
		if(write_buffer.hasRemaining())
		{
			write_buffer.compact();
			TaskUtils.setSocketReadyToWrite(key);
		}
		else
		{
			write_buffer.clear();
			TaskUtils.setSocketReadyToRead(key);
		}
	}
	
	public void fillWriteBuffer(byte[] data)
	{
		write_buffer.putInt(data.length);
		write_buffer.put(data);
	}
}

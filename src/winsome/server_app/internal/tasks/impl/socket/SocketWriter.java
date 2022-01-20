package winsome.server_app.internal.tasks.impl.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

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
		if(write_buffer.hasRemaining())
		{
			((SocketChannel)key.channel()).write(write_buffer);
			write_buffer.compact();
			key.interestOps(SelectionKey.OP_WRITE);
		}
		else
		{
			write_buffer.clear();
			key.interestOps(SelectionKey.OP_READ);
		}
	}
	
	public void fillWriteBuffer(byte[] data)
	{
		write_buffer.putInt(data.length);
		write_buffer.put(data);
	}
}

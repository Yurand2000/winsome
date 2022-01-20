package winsome.server_app.internal.tasks.impl.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SocketReader
{	
	private final SelectionKey key;
	private final ByteBuffer read_buffer;
	private byte[] read_data;
	private int dataSize, executedOperations;
	
	public SocketReader(SelectionKey key)
	{
		this.key = key;
		this.read_buffer = ByteBuffer.allocate(512);
		this.read_data = null;
		this.dataSize = -1;
		this.executedOperations = 0;
	}
	
	public void executeReadOperation()
	{
		try
		{
			tryExecuteReadOperation();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void tryExecuteReadOperation() throws IOException
	{
		fillReadingBuffer();
		
		do
		{
			resetExecutedCounter();
			if(needsReadingMessageLength())
			{
				tryReadMessageLength();
			}
			else
			{
				tryReadMessage();
			}
		}
		while(hasBytesToRead() && hasAnyOperationBeenExecuted());
		
		if(!hasMessageBeenRetrived())
		{
			key.interestOps(SelectionKey.OP_READ);
		}
	}
	
	public boolean hasMessageBeenRetrived()
	{
		return read_data != null;
	}
	
	public byte[] getRetrivedMessage()
	{
		assert(hasMessageBeenRetrived());
		
		byte[] temp = read_data;
		read_data = null;
		return temp;
	}
	
	private void fillReadingBuffer() throws IOException
	{
		((SocketChannel)key.channel()).read(read_buffer);
	}
	
	private boolean needsReadingMessageLength()
	{
		return dataSize < 0;
	}
	
	private void tryReadMessageLength() throws IOException
	{
		if(hasReadAtLeastBytes(Integer.BYTES))			
		{
			increaseExecutedCounter();
			read_buffer.flip();
			dataSize = read_buffer.getInt();
			read_buffer.compact();
		}
	}
	
	private void tryReadMessage() throws IOException
	{
		if(hasReadAtLeastBytes(dataSize))
		{
			increaseExecutedCounter();

			read_data = new byte[dataSize];
			read_buffer.flip();
			read_buffer.get(read_data);
			read_buffer.compact();
			dataSize = -1;
		}
	}
	
	private boolean hasReadAtLeastBytes(int bytes) throws IOException
	{
		return read_buffer.position() >= bytes;
	}
	
	private boolean hasBytesToRead() throws IOException
	{
		return read_buffer.position() > 0;
	}
	
	private boolean hasAnyOperationBeenExecuted()
	{
		return executedOperations > 0;
	}
	
	private void resetExecutedCounter()
	{
		executedOperations = 0;
	}
	
	private void increaseExecutedCounter()
	{
		executedOperations++;
	}
}

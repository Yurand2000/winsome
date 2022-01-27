package winsome.connection.server_api.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SocketReaderImpl implements SocketReader
{	
	private final SelectionKey key;
	private final ByteBuffer read_buffer;
	private byte[] read_data;
	private int dataSize;
	private boolean hasExecutedOperations;
	
	public SocketReaderImpl(SelectionKey key)
	{
		this.key = key;
		this.read_buffer = ByteBuffer.allocate(16384);
		this.read_data = null;
		this.dataSize = -1;
		this.hasExecutedOperations = false;
	}
	
	@Override
	public void executeReadOperation() throws IOException
	{
		try
		{
			tryExecuteReadOperation();
		}
		catch (IOException e)
		{
			SocketUtils.destroyKey(key);
			throw e;
		}
	}
	
	private void tryExecuteReadOperation() throws IOException
	{
		fillReadingBuffer();
		
		do
		{
			hasExecutedOperations = false;
			if(needsReadingMessageLength())
			{
				tryReadMessageLength();
			}
			else
			{
				tryReadMessage();
			}
		}
		while(!hasMessageBeenRetrived() && hasBytesToRead() && hasExecutedOperations);
		
		if(!hasMessageBeenRetrived())
		{
			SocketUtils.setSocketReadyToRead(key);
		}
	}

	@Override
	public boolean hasMessageBeenRetrived()
	{
		return read_data != null;
	}

	@Override
	public byte[] getRetrivedMessage()
	{		
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
			hasExecutedOperations = true;
			
			read_buffer.flip();
			dataSize = read_buffer.getInt();
			read_buffer.compact();
		}
	}
	
	private void tryReadMessage() throws IOException
	{
		if(hasReadAtLeastBytes(dataSize))
		{
			hasExecutedOperations = true;

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
}

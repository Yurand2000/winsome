package winsome.connection.server_api.socket.tasks.test;

import java.io.IOException;

import winsome.connection.server_api.socket.SocketReader;
import winsome.connection.socket_messages.Message;
import winsome.generic.SerializerWrapper;

class SocketReaderTest implements SocketReader
{
	private boolean read_executed = false;
	private byte[] message;

	public SocketReaderTest() throws IOException
	{
		message = SerializerWrapper.serializeCompact(new Message());
	}
	
	@Override
	public void executeReadOperation()
	{
		read_executed = true;
	}

	@Override
	public boolean hasMessageBeenRetrived()
	{
		return read_executed;
	}

	@Override
	public byte[] getRetrivedMessage()
	{
		return message;
	}

	public void setRetrivedMessageToUnknown()
	{
		message = "unknown".getBytes();
	}
}

package winsome.server_app.internal.tasks.impl.socket;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import winsome.connection.socket_messages.Message;
import winsome.generic.SerializerWrapper;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.tasks.impl.ParseIncomingMessageTask;

public class Task_SocketRead implements WinsomeTask
{
	private SelectionKey key;
	
	public Task_SocketRead(SelectionKey key)
	{
		this.key = key;
	}

	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		SocketInformations data = (SocketInformations) key.attachment();
		data.reader.executeReadOperation();
		
		if(data.reader.hasMessageBeenRetrived())
		{
			byte[] message_data = data.reader.getRetrivedMessage();
			Message message = deserializeMessage(message_data);
			server.executeTask( new ParseIncomingMessageTask(key, message) );
		}
	}
	
	private Message deserializeMessage(byte[] data)
	{
		try
		{
			return SerializerWrapper.deserialize(data, Message.class);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
}

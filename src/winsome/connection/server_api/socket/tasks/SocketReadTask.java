package winsome.connection.server_api.socket.tasks;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketState;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.generic.SerializerWrapper;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.tasks.impl.ParseIncomingMessageTask;

public class SocketReadTask extends SocketTask
{	
	public SocketReadTask(SelectionKey key)
	{
		super(key);
	}

	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		SocketState data = (SocketState) key.attachment();
		data.getReader().executeReadOperation();
		
		if(data.getReader().hasMessageBeenRetrived())
		{
			byte[] message_data = data.getReader().getRetrivedMessage();
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
			TaskUtils.sendMessage((SocketState) key.attachment(), new RequestExceptionAnswer(e.getMessage()));
			TaskUtils.setSocketReadyToWrite(key);
			throw new RuntimeException(e.getMessage());
		}
	}
}

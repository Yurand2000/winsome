package winsome.connection.server_api.socket.tasks;

import java.io.IOException;
import winsome.connection.server_api.socket.SocketState;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.generic.SerializerWrapper;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.impl.ParseIncomingMessageTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class SocketReadTask extends SocketTask
{	
	public SocketReadTask(SocketState socket, WinsomeData data)
	{
		super(socket, data);
	}

	@Override
	public void run(ServerThreadpool pool)
	{
		try
		{
			socket.getReader().executeReadOperation();
		}
		catch (IOException e)
		{
			socket.cleanupSocketState();
			return;
		}
		
		if(socket.getReader().hasMessageBeenRetrived())
		{
			byte[] message_data = socket.getReader().getRetrivedMessage();
			socket.setRequestMessage(deserializeMessage(message_data));
			pool.enqueueTask( new ParseIncomingMessageTask(socket, data) );
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
			socket.sendAnswerMessage( new RequestExceptionAnswer(e.toString()) );
			throw new RuntimeException(e.getMessage());
		}
	}
}

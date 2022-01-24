package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.client.*;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class ParseIncomingMessageTask extends SocketClientTask
{
	public ParseIncomingMessageTask(SocketTaskState socket, WinsomeData data)
	{
		super(socket, data);
	}
	
	@Override
	public void execute(ServerThreadpool pool)
	{
		Message message = socket.getRequestMessage();
		if(message.getClass() == LoginRequest.class)
		{
			pool.enqueueTask(new LoginUserTask(socket, data, (LoginRequest) message));
		}
		else if(message.getClass() == LogoutRequest.class)
		{
			pool.enqueueTask(new LogoutUserTask(socket, data, (LogoutRequest) message));
		}
		else
		{
			throw new RuntimeException("Unknown task.");
		}
	}
}

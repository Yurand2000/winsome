package winsome.server_app.internal.tasks.impl;

import java.nio.channels.SelectionKey;

import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.client.*;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;

public class ParseIncomingMessageTask implements WinsomeTask
{
	private final SelectionKey socket;
	private final Message message;

	public ParseIncomingMessageTask(SelectionKey socket, Message message)
	{
		this.socket = socket;
		this.message = message;
	}
	
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		if(message.getClass() == LoginRequest.class)
		{
			server.executeTask(new LoginUserTask(socket, (LoginRequest) message));
		}
		else if(message.getClass() == LogoutRequest.class)
		{
			server.executeTask(new LogoutUserTask(socket, (LogoutRequest) message));
		}
		else
		{
			server.executeTask(new UnknownTask(socket));
		}
	}
}

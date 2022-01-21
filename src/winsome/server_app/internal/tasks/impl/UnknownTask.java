package winsome.server_app.internal.tasks.impl;

import java.nio.channels.SelectionKey;

import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.tasks.impl.socket.SocketInformations;

public class UnknownTask implements WinsomeTask
{
	private final SelectionKey socket;
	
	public UnknownTask(SelectionKey socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		TaskUtils.sendMessage(
			(SocketInformations) socket.attachment(),
			new RequestExceptionAnswer("Unknown task.")
		);
		
		TaskUtils.setSocketReadyToWrite(socket);
	}
}

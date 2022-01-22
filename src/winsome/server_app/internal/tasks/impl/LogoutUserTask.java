package winsome.server_app.internal.tasks.impl;

import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketInformations;
import winsome.connection.socket_messages.client.LogoutRequest;
import winsome.connection.socket_messages.server.LogoutAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.tasks.WinsomeTask;

public class LogoutUserTask implements WinsomeTask
{
	private final SelectionKey socket;
	@SuppressWarnings("unused")
	private final LogoutRequest message;

	public LogoutUserTask(SelectionKey socket, LogoutRequest message)
	{
		this.socket = socket;
		this.message = message;
	}

	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		logout();		
		TaskUtils.sendMessage((SocketInformations) socket.attachment(), new LogoutAnswer());		
		TaskUtils.setSocketReadyToWrite(socket);
	}
	
	private void logout()
	{
		SocketInformations infos = (SocketInformations) socket.attachment();
		if(infos.getSocketUser() == null)
		{
			TaskUtils.sendMessage(infos, new RequestExceptionAnswer("User not logged in."));
		}
		
		infos.setSocketUser(null);
	}
}

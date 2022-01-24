package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.LogoutRequest;
import winsome.connection.socket_messages.server.LogoutAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class LogoutUserTask extends SocketClientTask
{
	@SuppressWarnings("unused")
	private final LogoutRequest message;

	public LogoutUserTask(SocketTaskState socket, WinsomeData data, LogoutRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void execute(ServerThreadpool pool)
	{
		if(socket.getClientUser() == null)
		{
			throw new RuntimeException("User not logged in.");
		}
		else
		{
			socket.unsetClientUser();
			socket.sendAnswerMessage(new LogoutAnswer());
		}		
	}
}

package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.LogoutRequest;
import winsome.connection.socket_messages.server.LogoutAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketClientTask;
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
	public void run(ServerThreadpool pool)
	{
		logout();		
		socket.sendAnswerMessage(new LogoutAnswer());
	}
	
	private void logout()
	{
		if(socket.getClientUser() == null)
		{
			socket.sendAnswerMessage( new RequestExceptionAnswer("User not logged in."));
		}
		
		socket.setClientUser(null);
	}
}

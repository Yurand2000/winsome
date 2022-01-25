package winsome.connection.server_api.socket.tasks;

import java.io.IOException;

import winsome.connection.server_api.socket.SocketState;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class SocketWriteTask extends SocketTask
{	
	public SocketWriteTask(SocketState socket, WinsomeData data)
	{
		super(socket, data);
	}
	
	@Override
	public void run(ServerThreadpool pool)
	{
		try
		{
			socket.getWriter().executeWriteOperation();
		}
		catch (IOException e)
		{
			socket.cleanupSocketState();
		}
	}

}

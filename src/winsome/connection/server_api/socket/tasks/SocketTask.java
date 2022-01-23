package winsome.connection.server_api.socket.tasks;

import winsome.connection.server_api.socket.SocketState;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.WinsomeTask;

public abstract class SocketTask extends WinsomeTask
{
	protected SocketState socket;
	
	public SocketTask(SocketState socket, WinsomeData data)
	{
		super(data);
		this.socket = socket;
	}
}

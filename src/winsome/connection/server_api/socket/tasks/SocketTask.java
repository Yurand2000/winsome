package winsome.connection.server_api.socket.tasks;

import java.nio.channels.SelectionKey;

import winsome.server_app.internal.tasks.WinsomeTask;

public abstract class SocketTask implements WinsomeTask
{
	protected SelectionKey key;
	
	public SocketTask(SelectionKey key)
	{
		this.key = key;
	}
}

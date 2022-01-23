package winsome.server_app.internal.tasks;

import winsome.server_app.internal.WinsomeData;

public abstract class SocketClientTask extends WinsomeTask
{
	protected final SocketTaskState socket;

	public SocketClientTask(SocketTaskState socket, WinsomeData data)
	{
		super(data);
		this.socket = socket;
	}
}

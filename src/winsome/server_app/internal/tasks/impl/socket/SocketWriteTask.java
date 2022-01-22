package winsome.server_app.internal.tasks.impl.socket;

import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketState;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;

public class SocketWriteTask implements WinsomeTask
{
	private SelectionKey key;
	
	public SocketWriteTask(SelectionKey key)
	{
		this.key = key;
	}
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		SocketState data = (SocketState) key.attachment();
		data.writer.executeWriteOperation();
	}

}

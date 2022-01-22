package winsome.server_app.internal.tasks.impl.socket;

import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketInformations;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.WinsomeTask;

public class Task_SocketWrite implements WinsomeTask
{
	private SelectionKey key;
	
	public Task_SocketWrite(SelectionKey key)
	{
		this.key = key;
	}
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		SocketInformations data = (SocketInformations) key.attachment();
		data.writer.executeWriteOperation();
	}

}

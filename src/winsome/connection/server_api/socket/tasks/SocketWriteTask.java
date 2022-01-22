package winsome.connection.server_api.socket.tasks;

import java.nio.channels.SelectionKey;

import winsome.connection.server_api.socket.SocketState;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.WinsomeServer;

public class SocketWriteTask extends SocketTask
{	
	public SocketWriteTask(SelectionKey key)
	{
		super(key);
	}
	
	@Override
	public void run(WinsomeServer server, WinsomeData server_data)
	{
		SocketState data = (SocketState) key.attachment();
		data.getWriter().executeWriteOperation();
	}

}

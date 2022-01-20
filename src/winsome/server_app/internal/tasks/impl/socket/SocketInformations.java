package winsome.server_app.internal.tasks.impl.socket;

import java.nio.channels.SelectionKey;

public class SocketInformations
{
	private String username;
	public final SocketReader reader;
	public final SocketWriter writer;
	
	public SocketInformations(SelectionKey socket)
	{
		reader = new SocketReader(socket);
		writer = new SocketWriter(socket);
		username = null;
	}
	
	public String getSocketUser()
	{
		return username;
	}
	
	public void setSocketUser(String username)
	{
		this.username = username;
	}
}

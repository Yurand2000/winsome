package winsome.connection.server_api.socket;

import java.nio.channels.SelectionKey;

public class SocketStateImpl implements SocketState
{
	private String username;
	private final SocketReader reader;
	private final SocketWriter writer;
	
	public SocketStateImpl(SelectionKey socket)
	{
		reader = new SocketReaderImpl(socket);
		writer = new SocketWriterImpl(socket);
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

	@Override
	public SocketReader getReader()
	{
		return reader;
	}

	@Override
	public SocketWriter getWriter()
	{
		return writer;
	}
}

package winsome.connection.server_api.socket;

import java.nio.channels.SelectionKey;

import winsome.connection.socket_messages.Message;

public class SocketStateImpl implements SocketState
{
	private Message incoming_message;
	private String username;
	private final SocketReader reader;
	private final SocketWriter writer;
	private final SelectionKey socket;
	
	public SocketStateImpl(SelectionKey key)
	{
		socket = key;
		reader = new SocketReaderImpl(socket);
		writer = new SocketWriterImpl(socket);
		incoming_message = null;
		username = null;
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

	@Override
	public String getClientUser()
	{
		return username;
	}

	@Override
	public void setClientUser(String username)
	{
		this.username = username;		
	}

	@Override
	public void unsetClientUser()
	{
		this.username = null;		
	}

	@Override
	public Message getRequestMessage()
	{
		return incoming_message;
	}
	
	@Override
	public void setRequestMessage(Message incoming)
	{
		incoming_message = incoming;
	}

	@Override
	public void sendAnswerMessage(Message answer)
	{
		SocketUtils.sendMessage(this, answer);
		SocketUtils.setSocketReadyToWrite(socket);
		incoming_message = null;
	}
}

package winsome.connection.server_api.socket;

import java.nio.channels.SelectionKey;

import winsome.connection.socket_messages.Message;

public class SocketStateImpl implements SocketState
{
	private Message incoming_message;
	private String local_socket_user;
	private final SocketReader reader;
	private final SocketWriter writer;
	private final SelectionKey socket;
	private final SocketStateCommon common_state;
	
	public SocketStateImpl(SelectionKey key, SocketStateCommon common)
	{
		socket = key;
		common_state = common;
		reader = new SocketReaderImpl(socket);
		writer = new SocketWriterImpl(socket);
		incoming_message = null;
		local_socket_user = null;
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
	public void cleanupSocketState()
	{
		try
		{
			common_state.setUserLoggedOut(local_socket_user);
			local_socket_user = null;
		}
		catch(RuntimeException e) { }
	}
	
	@Override
	public void setRequestMessage(Message incoming)
	{
		incoming_message = incoming;
	}
	
	

	@Override
	public String getClientUser()
	{
		return local_socket_user;
	}

	@Override
	public void setClientUser(String username)
	{
		common_state.setUserLoggedIn(username);
		local_socket_user = username;
	}

	@Override
	public void unsetClientUser()
	{
		common_state.setUserLoggedOut(local_socket_user);
		local_socket_user = null;
	}

	@Override
	public Message getRequestMessage()
	{
		return incoming_message;
	}

	@Override
	public void sendAnswerMessage(Message answer)
	{
		SocketUtils.sendMessage(this, answer);
		SocketUtils.setSocketReadyToWrite(socket);
		incoming_message = null;
	}
}

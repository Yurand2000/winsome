package winsome.client_app.internal.tasks.test;

import java.io.IOException;

import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer.Exception;

class ConnectionHandlerTest implements ConnectionHandler
{
	public boolean connect_called = false;
	public boolean disconnect_called = false;
	public Message sent_message = null;
	private Message receive_message = null;
	
	public ConnectionHandlerTest()
	{
		
	}

	@Override
	public void connect() throws IOException
	{
		connect_called = true;
	}

	public void setReceiveMessage(Message msg)
	{
		receive_message = msg;
	}
	
	@Override
	public <T extends Message> T readMessage(Class<T> type) throws IOException, ServerInternalException, Exception
	{
		if(type == receive_message.getClass())
		{
			return type.cast(receive_message);
		}
		else if(receive_message.getClass() == RequestExceptionAnswer.class)
		{
			throw RequestExceptionAnswer.class.cast(receive_message).makeException();
		}
		else
		{
			throw new ServerInternalException("couldn't read message");
		}
	}

	@Override
	public <T extends Message> void sendMessage(T message) throws IOException
	{
		sent_message = message;
	}

	@Override
	public void disconnect()
	{
		disconnect_called = true;
	}

}

package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import winsome.connection.server_api.socket.SocketState;
import winsome.connection.socket_messages.Message;

class SocketStateTest implements SocketState
{
	private SocketReaderTest reader;
	private SocketWriterTest writer;
	
	public SocketStateTest() throws IOException
	{
		reader = new SocketReaderTest();
		writer = new SocketWriterTest();
	}

	@Override
	public SocketReaderTest getReader()
	{
		return reader;
	}

	@Override
	public SocketWriterTest getWriter()
	{
		return writer;
	}

	@Override
	public String getClientUser() { fail(); return null; }

	@Override
	public void setClientUser(String username) { fail(); }

	@Override
	public void cleanupSocketState() { fail(); }
	
	@Override
	public void unsetClientUser() { fail(); }

	public boolean setRequestMessage_executed = false;
	private Message requestMessage = null;
	
	@Override
	public void setRequestMessage(Message incoming)
	{
		setRequestMessage_executed = true;
		requestMessage = incoming;
	}

	@Override
	public Message getRequestMessage()
	{
		return requestMessage;
	}

	private boolean send_answer_invoked = false;
	
	@Override
	public void sendAnswerMessage(Message answer)
	{
		send_answer_invoked = true;
	}
	
	public void checkSendAnswerInvoked()
	{
		assertTrue(send_answer_invoked);
		send_answer_invoked = false;
	}
}

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
	public void setClientUser(String username) { }

	@Override
	public Message getRequestMessage() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void setRequestMessage(Message incoming) {
		// TODO Auto-generated method stub
		
	}
}

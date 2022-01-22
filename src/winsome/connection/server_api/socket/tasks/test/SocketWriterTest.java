package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.connection.server_api.socket.SocketWriter;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.generic.SerializerWrapper;

class SocketWriterTest implements SocketWriter
{
	private boolean send_message_called = false;
	private boolean write_executed = false;
	
	public SocketWriterTest() { }
	
	@Override
	public void executeWriteOperation()
	{
		write_executed = true;
	}
	
	public void checkWriteExecuted()
	{
		assertTrue(write_executed);
		write_executed = false;
	}

	@Override
	public void addMessageToSend(byte[] data)
	{
		assertDoesNotThrow(() ->
		{
			SerializerWrapper.deserialize(data, RequestExceptionAnswer.class);
		});
		send_message_called = true;
	}
	
	public void checkSendMessageCalled()
	{
		assertTrue(send_message_called);
		send_message_called = false;
	}
}

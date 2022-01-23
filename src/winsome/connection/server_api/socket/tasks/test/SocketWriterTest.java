package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.connection.server_api.socket.SocketWriter;

class SocketWriterTest implements SocketWriter
{
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
		fail();
	}
}

package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import winsome.connection.server_api.socket.SocketState;

class SocketStateTest implements SocketState
{
	private SocketReaderTest reader;
	private SocketWriterTest writer;
	
	public SocketStateTest() throws IOException
	{
		reader = new SocketReaderTest();
		writer = new SocketWriterTest();
	}

	public String getSocketUser() { fail(); return null; }
	public void setSocketUser(String username) { fail(); }

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
}

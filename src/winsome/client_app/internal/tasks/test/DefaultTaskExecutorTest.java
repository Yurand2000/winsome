package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.client_app.internal.tasks.DefaultTaskExecutor;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;

class DefaultTaskExecutorTest extends DefaultTaskExecutor
{
	private boolean execute_called = false;
	private boolean on_exception_called = false;
	private boolean on_finally_called = false;
	
	public boolean throw_io_exception = false;
	public boolean throw_server_internal_exception = false;
	public boolean throw_request_exception_answer = false;
	
	private ConnectionHandler connection = null;
	private ApplicationLoggedAPI api = null;
	
	public DefaultTaskExecutorTest()
	{
		
	}
	
	public void setExecuteArguments(ConnectionHandler connection, ApplicationLoggedAPI api)
	{
		this.connection = connection;
		this.api = api;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		execute_called = true;
		
		if(throw_io_exception) throw new IOException();
		else if(throw_server_internal_exception) throw new ServerInternalException("");
		else if(throw_request_exception_answer) throw new RequestExceptionAnswer.Exception("");
		
		assertEquals(connection, this.connection);
		assertEquals(api, this.api);
	}
	
	public void checkExecuteCalled()
	{
		assertTrue(execute_called);
		execute_called = false;
	}

	@Override
	protected void onException(ConnectionHandler connection, ApplicationLoggedAPI api)
	{
		on_exception_called = true;
	}
	
	public void checkOnExceptionCalled()
	{
		assertTrue(on_exception_called);
		on_exception_called = false;
	}
	
	public void checkOnExceptionNotCalled()
	{
		assertFalse(on_exception_called);
	}

	@Override
	protected void onFinally(ConnectionHandler connection, ApplicationLoggedAPI api)
	{
		on_finally_called = true;
	}
	
	public void checkOnFinallyCalled()
	{
		assertTrue(on_finally_called);
		on_finally_called = false;
	}
}

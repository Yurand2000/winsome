package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.client_app.api.APIException;
import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;

public abstract class DefaultTaskExecutor implements ClientTaskExecutor
{
	public DefaultTaskExecutor() { }

	@Override
	public void run(ConnectionHandler connection, ApplicationLoggedAPI api)
	{
		try
		{
			execute(connection, api);
		}
		catch (IOException e)
		{
			onException(connection, api);
			throw new APIException(e.toString());
		}
		catch (ServerInternalException e)
		{
			onException(connection, api);
			throw e;
		}
		catch (RequestExceptionAnswer.Exception e)
		{
			onException(connection, api);
			throw parseRequestException(e);
		}
		finally
		{
			onFinally(connection, api);
		}
	}

	protected abstract void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException;
	
	protected void onException(ConnectionHandler connection, ApplicationLoggedAPI api) { }
	
	protected void onFinally(ConnectionHandler connection, ApplicationLoggedAPI api) { }
	
	private APIException parseRequestException(RequestExceptionAnswer.Exception e)
	{
		// TODO
		return e;
	}
}

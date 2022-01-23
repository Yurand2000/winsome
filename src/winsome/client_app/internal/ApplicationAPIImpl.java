package winsome.client_app.internal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;

import winsome.client_app.api.APIException;
import winsome.client_app.api.ApplicationAPI;
import winsome.client_app.api.LoggedClientAPI;
import winsome.client_app.api.exceptions.NotLoggedInException;
import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.client_api.registrator.RegistratorRMIHandler;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.client_api.socket.ConnectionHandlerImpl;
import winsome.connection.client_api.socket.tasks.ClientTaskExecutor;
import winsome.connection.client_api.socket.tasks.LoginTaskExecutor;
import winsome.connection.client_api.socket.tasks.LogoutTaskExecutor;

public class ApplicationAPIImpl implements ApplicationAPI
{
	private final InetSocketAddress server_address;
	private ApplicationLoggedAPIImpl logged_client;
	private ConnectionHandler client_connection;
	private Runnable wallet_notification_runnable;
	
	public ApplicationAPIImpl(InetSocketAddress server_address, Runnable wallet_notification_runnable)
	{
		this.server_address = server_address;
		this.logged_client = null;
		this.client_connection = null;
		this.wallet_notification_runnable = wallet_notification_runnable;
	}

	@Override
	public void register(String username, String password, String[] tags)
	{		
		RegistratorRMIHandler.register(server_address.getHostString(), username, password, tags);
	}

	@Override
	public void login(String username, String password)
	{
		try
		{
			client_connection = new ConnectionHandlerImpl(server_address);
			logged_client = new ApplicationLoggedAPIImpl(server_address.getHostString(), username);
			
			executeTask(new LoginTaskExecutor(username, password, wallet_notification_runnable));			
		}
		catch (IOException | NotBoundException e)
		{
			logged_client = null;
			client_connection = null;
			
			throw new ServerInternalException(e.getMessage());
		}
		catch(APIException e)
		{
			logged_client = null;
			client_connection = null;
			
			throw e;
		}
	}

	@Override
	public void logout()
	{
		try
		{
			if(logged_client == null)
			{
				throw new NotLoggedInException();
			}
			executeTask(new LogoutTaskExecutor());
		} 
		catch (APIException e)
		{
			throw e;
		}
		finally
		{
			logged_client = null;
			client_connection = null;
		}
	}

	@Override
	public LoggedClientAPI getLoggedAPI()
	{
		if(logged_client == null)
		{
			throw new NotLoggedInException();
		}
		return logged_client;
	}

	private void executeTask(ClientTaskExecutor task)
	{
		task.run(client_connection, logged_client);
	}
}

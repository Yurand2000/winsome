package winsome.client_app.internal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;

import winsome.client_app.api.ApplicationAPI;
import winsome.client_app.api.LoggedClientAPI;
import winsome.client_app.api.exceptions.CouldNotConnectException;
import winsome.client_app.api.exceptions.NotLoggedInException;
import winsome.client_app.internal.tasks.ClientTaskExecutor;
import winsome.client_app.internal.tasks.LoginTaskExecutor;
import winsome.client_app.internal.tasks.LogoutTaskExecutor;
import winsome.connection.client_api.registrator.RegistratorRMIHandler;

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
			logged_client = new ApplicationLoggedAPIImpl(server_address.getHostString(), username);
			client_connection = new ConnectionHandler(server_address);
			client_connection.connect();
			
			executeTask(new LoginTaskExecutor(username, password, wallet_notification_runnable));
		}
		catch (IOException | NotBoundException e)
		{
			logged_client = null;
			client_connection = null;
			
			throw new CouldNotConnectException();
		}
	}

	@Override
	public void logout()
	{
		if(logged_client != null)
		{
			actualLogout();
		}
		else
		{
			throw new NotLoggedInException();
		}
	}
	
	private void actualLogout()
	{
		try
		{
			executeTask(new LogoutTaskExecutor());
			
			client_connection.disconnect();
		} 
		catch (IOException e) { }
		finally
		{
			logged_client = null;
			client_connection = null;
		}
	}

	@Override
	public LoggedClientAPI getLoggedAPI()
	{
		return logged_client;
	}

	private void executeTask(ClientTaskExecutor task)
	{
		task.run(client_connection, logged_client);
	}
}

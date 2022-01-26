package winsome.client_app.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.net.UnknownHostException;

import winsome.client_app.api.APIException;
import winsome.client_app.api.ApplicationAPI;
import winsome.client_app.api.LoggedClientAPI;
import winsome.client_app.api.exceptions.AlreadyLoggedInException;
import winsome.client_app.api.exceptions.NotLoggedInException;
import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.client_app.internal.tasks.ClientTaskExecutor;
import winsome.client_app.internal.tasks.LoginTaskExecutor;
import winsome.client_app.internal.tasks.LogoutTaskExecutor;
import winsome.connection.client_api.registrator.RegistratorRMIHandler;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.client_api.socket.ConnectionHandlerImpl;

public class ApplicationAPIImpl implements ApplicationAPI
{
	private final InetSocketAddress server_address;
	private final Integer server_rmi_port;
	private ApplicationLoggedAPIImpl logged_client;
	private ConnectionHandler client_connection;
	private Runnable wallet_notification_runnable;
	
	public ApplicationAPIImpl(InetSocketAddress server_address, Integer rmi_port, Runnable wallet_notification_runnable)
	{
		this.server_address = server_address;
		this.server_rmi_port = rmi_port;
		this.logged_client = null;
		this.client_connection = null;
		this.wallet_notification_runnable = wallet_notification_runnable;
		setRMIHostnameToLocalhost();
	}
	
	private void setRMIHostnameToLocalhost()
	{
		try { System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostName()); }
		catch (UnknownHostException e) { }
	}

	@Override
	public void register(String username, String password, String[] tags)
	{		
		RegistratorRMIHandler.register(server_address.getHostString(), server_rmi_port, username, password, tags);
	}

	@Override
	public void login(String username, String password)
	{
		if(logged_client == null)
		{
			try
			{
				
					client_connection = new ConnectionHandlerImpl(server_address);
					logged_client = new ApplicationLoggedAPIImpl(server_address.getHostString(), server_rmi_port, username, client_connection);
					
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
		else
		{
			throw new AlreadyLoggedInException();
		}
	}

	@Override
	public void logout()
	{
		if(logged_client != null)
		{
			try
			{
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
		else
		{
			throw new NotLoggedInException();
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

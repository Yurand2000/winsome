package winsome.client_app;

import java.net.InetSocketAddress;

import winsome.client_app.api.*;
import winsome.client_app.internal.ApplicationAPIImpl;

public class ClientAppAPI
{
	private static ApplicationAPI client_api = null;
	
	public static ApplicationAPI getAPI()
	{
		return client_api;
	}
	
	public static LoggedClientAPI getLoggedClientAPI()
	{
		return client_api.getLoggedAPI();
	}
	
	public static void startClient(InetSocketAddress server_address)
	{
		client_api = new ApplicationAPIImpl(server_address);
	}
	
	private ClientAppAPI() { }
}

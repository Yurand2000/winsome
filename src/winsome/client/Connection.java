package winsome.client;

import winsome.client.api.*;

public class Connection
{
	private static ClientAPI client_api = null;
	
	public static ClientAPI getAPI()
	{
		return client_api;
	}
	
	public static LoggedClientAPI getLoggedAPI()
	{
		return client_api.getLoggedAPI();
	}
	
	static void setClientAPI(ClientAPI api)
	{
		client_api = api;
	}
	
	private Connection() { }
}

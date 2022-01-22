package winsome.client_app;

import java.net.UnknownHostException;

import winsome.client_app.api.*;
import winsome.client_app.internal.ApplicationAPIImpl;
import winsome.connection.socket_messages.MessageUtils;

public class ClientAppAPI
{
	private static final String client_settings_file = "settings.json";
	
	private static ApplicationAPI client_api = null;
	
	public static ApplicationAPI getAPI()
	{
		return client_api;
	}
	
	public static LoggedClientAPI getLoggedClientAPI()
	{
		return client_api.getLoggedAPI();
	}
	
	public static void startClient(Runnable wallet_notification) throws UnknownHostException
	{
		MessageUtils.registerJsonDeserializers();
		
		ClientSettings settings = getClientSettings();
		client_api = new ApplicationAPIImpl(settings.makeServerAdddress(), wallet_notification);
	}
	
	public static void stopClient()
	{
		if(client_api != null)
		{
			client_api.logout();
			client_api = null;
		}
	}
	
	public static ClientSettings getClientSettings()
	{
		ClientSettings settings = ClientSettings.deserializeFromFile(client_settings_file);
		if(settings == null)
		{
			settings = new ClientSettings();
			ClientSettings.serializeToFile(settings, client_settings_file);
		}
		return settings;
	}
	
	private ClientAppAPI() { }
}

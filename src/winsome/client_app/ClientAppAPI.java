package winsome.client_app;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import winsome.client_app.api.*;
import winsome.client_app.internal.ApplicationAPIImpl;
import winsome.generic.SerializerWrapper;

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
		Path path = Paths.get(client_settings_file);
		
		try
		{
			byte[] settings_data = Files.readAllBytes(path);
			return SerializerWrapper.deserialize(settings_data, ClientSettings.class);
		}
		catch (IOException e)
		{
			ClientSettings settings = new ClientSettings();
			
			try 
			{
				byte[] settings_data = SerializerWrapper.serialize(settings);
				Files.write(path, settings_data);
			}
			catch (IOException e2) { }
			
			return settings;
		}
	}
	
	private ClientAppAPI() { }
}

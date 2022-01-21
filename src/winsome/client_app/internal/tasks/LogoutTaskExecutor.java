package winsome.client_app.internal.tasks;

import java.io.IOException;
import java.rmi.RemoteException;

import winsome.client_app.internal.ApplicationLoggedAPIImpl;
import winsome.client_app.internal.ConnectionHandler;
import winsome.connection.socket_messages.client.LogoutRequest;
import winsome.connection.socket_messages.server.LogoutAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer.RequestException;

public class LogoutTaskExecutor implements ClientTaskExecutor
{
	public LogoutTaskExecutor() { }

	@Override
	public void run(ConnectionHandler connection, ApplicationLoggedAPIImpl logged_api)
	{
		try
		{
			stopFollowerNotificationListener(logged_api);
			stopWalletNotificationListener(logged_api);
			
			LogoutRequest request = new LogoutRequest();
			connection.sendMessage(request);			
			connection.readMessage(LogoutAnswer.class);
		}
		catch (IOException e) { disconnect(connection); }
		catch (RequestException e) { disconnect(connection); }
	}
	
	private void stopWalletNotificationListener(ApplicationLoggedAPIImpl logged_api)
	{
		logged_api.wallet_notifier.unregisterWalletUpdateNotifications();
	}
	
	private void stopFollowerNotificationListener(ApplicationLoggedAPIImpl logged_api) throws RemoteException
	{
		logged_api.client_handler.unregisterFollowerUpdater();
	}
	
	private void disconnect(ConnectionHandler connection)
	{
		try { connection.disconnect(); }
		catch (IOException e) { }
	}
}

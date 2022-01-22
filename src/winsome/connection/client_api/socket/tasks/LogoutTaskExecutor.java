package winsome.connection.client_api.socket.tasks;

import java.io.IOException;

import winsome.connection.client_api.socket.*;
import winsome.connection.socket_messages.client.*;
import winsome.connection.socket_messages.server.*;

public class LogoutTaskExecutor extends DefaultTaskExecutor
{
	public LogoutTaskExecutor() { }
	
	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		stopFollowerNotificationListener(api);
		stopWalletNotificationListener(api);
		
		LogoutRequest request = new LogoutRequest();
		connection.sendMessage(request);			
		connection.readMessage(LogoutAnswer.class);
	}

	@Override
	protected void onFinally(ConnectionHandler connection, ApplicationLoggedAPI api)
	{
		connection.disconnect();
	}
	
	private void stopWalletNotificationListener(ApplicationLoggedAPI api)
	{
		api.getWalletNotifier().unregisterWalletUpdateNotifications();
	}
	
	private void stopFollowerNotificationListener(ApplicationLoggedAPI api) throws IOException
	{
		api.getFollowerUpdater().unregisterFollowerUpdater();
	}
}

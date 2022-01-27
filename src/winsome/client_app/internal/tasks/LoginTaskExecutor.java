package winsome.client_app.internal.tasks;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;

import winsome.connection.client_api.socket.*;
import winsome.connection.socket_messages.client.*;
import winsome.connection.socket_messages.server.*;

public class LoginTaskExecutor extends DefaultTaskExecutor
{
	//request data
	private final String username;
	private final String password;
	private final Runnable wallet_notification_runnable;
	
	public LoginTaskExecutor(String username, String password, Runnable wallet_notification_runnable)
	{
		this.username = username;
		this.password = password;
		this.wallet_notification_runnable = wallet_notification_runnable;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		connection.connect();
		
		LoginRequest request = new LoginRequest(username, password);
		connection.sendMessage(request);
		
		LoginAnswer answer = connection.readMessage(LoginAnswer.class);
		fillStructures(api, answer);
		
		startWalletNotificationListener(api, answer);
		startFollowerNotificationListener(api);
	}

	@Override
	protected void onException(ConnectionHandler connection, ApplicationLoggedAPI api)
	{
		api.getFollowerUpdater().unregisterFollowerUpdater();
		api.getWalletNotifier().unregisterWalletUpdateNotifications();
		connection.disconnect();
	}

	private void fillStructures(ApplicationLoggedAPI api, LoginAnswer answer)
	{
		api.getFollowers().addAll(Arrays.asList(answer.followed_by_users));
		api.getFollowing().addAll(Arrays.asList(answer.following_users));
	}
	
	private void startWalletNotificationListener(ApplicationLoggedAPI api, LoginAnswer answer)
	{
		api.getWalletNotifier().registerWalletUpdateNotifications(
			answer.udp_multicast_address,
			answer.udp_multicast_port,
			wallet_notification_runnable
		);
	}
	
	private void startFollowerNotificationListener(ApplicationLoggedAPI api) throws RemoteException
	{
		api.getFollowerUpdater().registerFollowerUpdater();
	}
}

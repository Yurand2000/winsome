package winsome.client_app.internal.tasks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Arrays;

import winsome.client_app.api.PostShort;
import winsome.client_app.internal.ApplicationLoggedAPIImpl;
import winsome.client_app.internal.ConnectionHandler;
import winsome.connection.socket_messages.client.LoginRequest;
import winsome.connection.socket_messages.server.LoginAnswer;
import winsome.connection.socket_messages.server.LoginAnswer.PostIdAndTitle;
import winsome.connection.socket_messages.server.RequestExceptionAnswer.RequestException;

public class LoginTaskExecutor implements ClientTaskExecutor
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
	public void run(ConnectionHandler connection, ApplicationLoggedAPIImpl logged_api)
	{
		try
		{
			LoginRequest request = new LoginRequest(username, password);
			connection.sendMessage(request);
			
			LoginAnswer answer = connection.readMessage(LoginAnswer.class);
			fillStructures(logged_api, answer);
			
			startWalletNotificationListener(logged_api, answer);
			startFollowerNotificationListener(logged_api);
		}
		catch (IOException e) { disconnect(connection); }
		catch (RequestException e)
		{
			RuntimeException exc = parseRequestException(e);
			disconnect(connection);
			throw exc;
		}
	}

	private void fillStructures(ApplicationLoggedAPIImpl logged_api, LoginAnswer answer)
	{
		logged_api.followers.addAll(Arrays.asList(answer.followed_by_users));
		logged_api.following.addAll(Arrays.asList(answer.following_users));
		
		for(PostIdAndTitle post : answer.my_blog)
		{
			logged_api.blog.put(post.postId, new PostShort(post.postId, logged_api.me, post.title));
		}
	}
	
	private void startWalletNotificationListener(ApplicationLoggedAPIImpl logged_api, LoginAnswer answer) throws UnknownHostException
	{
		logged_api.wallet_notifier.registerWalletUpdateNotifications(
			InetAddress.getByName(answer.udp_multicast_address),
			wallet_notification_runnable);
	}
	
	private void startFollowerNotificationListener(ApplicationLoggedAPIImpl logged_api) throws RemoteException
	{
		logged_api.client_handler.registerFollowerUpdater();
	}
	
	private RuntimeException parseRequestException(RequestException e)
	{
		return e;
	}
	
	private void disconnect(ConnectionHandler connection)
	{
		try { connection.disconnect(); }
		catch (IOException e) { }
	}
}

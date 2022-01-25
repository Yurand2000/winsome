package winsome.server_app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.concurrent.TimeUnit;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandlerImpl;
import winsome.connection.server_api.registrator.RegistratorRMIHandler;
import winsome.connection.server_api.socket.ClientHandler;
import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdaterImpl;
import winsome.connection.socket_messages.MessageUtils;
import winsome.server_app.internal.*;
import winsome.server_app.post.PostUtils;

public class ServerMain
{
	public final ServerSettings settings;
	private final WinsomeServerImpl server;
	private final ServerAutosaver autosaver;
	private final RegistratorRMIHandler registrator_handler;
	private final ClientHandler client_handler;
	private final WalletNotificationUpdaterImpl wallet_notifier;
	private final FollowerUpdaterRegistratorHandlerImpl follower_updater;
	private final WalletUpdater wallet_updater;
	
	public ServerMain(String settings_file)
	{
		settings = getServerSettings(settings_file);
		InetSocketAddress server_address = getServerAddress();
		PostUtils.registerJsonDeserializers();
		MessageUtils.registerJsonDeserializers();
		
		wallet_notifier = new WalletNotificationUpdaterImpl(settings.server_udp_address, settings.server_udp_port);
		follower_updater = new FollowerUpdaterRegistratorHandlerImpl(settings.server_rmi_port);
		
		server = new WinsomeServerImpl(settings, follower_updater, wallet_notifier);		
		autosaver = new ServerAutosaver(server);
		wallet_updater = new WalletUpdater(server.getWinsomeData(), server.getThreadpool(), settings.reward_timer_in_seconds.longValue(), TimeUnit.SECONDS, settings.reward_author_part);
		registrator_handler = new RegistratorRMIHandler(server.getWinsomeData(), server.getThreadpool(), settings.server_rmi_port);
		client_handler = new ClientHandler(server_address, server.getWinsomeData(), server.getThreadpool());
	}
	
	public void startServer() throws IOException, AlreadyBoundException
	{
		ServerRMIRegistry.startRegistry(settings.server_rmi_port);
		server.startServer();
		
		autosaver.startAutosaver();
		wallet_updater.startUpdater();
		registrator_handler.bindObject();
		client_handler.startClientHandler();
		follower_updater.bindObject();
	}
	
	public void stopServer() throws InterruptedException, IOException, NotBoundException
	{
		follower_updater.unbindObject();
		client_handler.stopClientHandler();
		registrator_handler.unbindObject();
		wallet_updater.stopUpdater();
		autosaver.stopAutosaver();
		
		server.shutdownServer();
		ServerRMIRegistry.shutdownRegistry();
	}
	
	private ServerSettings getServerSettings(String settings_file)
	{
		ServerSettings settings = ServerSettings.deserializeFromFile(settings_file);
		if(settings == null)
		{
			settings = new ServerSettings();
			ServerSettings.serializeToFile(settings, settings_file);
		}
		return settings;
	}
	
	private InetSocketAddress getServerAddress()
	{
		try
		{
			return new InetSocketAddress(InetAddress.getByName(settings.server_address), settings.server_port);
		}
		catch (UnknownHostException e) { throw new RuntimeException(e.getMessage()); }
	}
}

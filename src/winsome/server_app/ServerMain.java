package winsome.server_app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.registrator.RegistratorRMIHandler;
import winsome.connection.server_api.socket.ClientHandler;
import winsome.connection.socket_messages.MessageUtils;
import winsome.server_app.internal.*;

public class ServerMain
{
	public final ServerSettings settings;
	private final WinsomeServerImpl server;
	private final ServerAutosaver autosaver;
	private final RegistratorRMIHandler registrator_handler;
	private final ClientHandler client_handler;
	private final FollowerUpdaterRegistratorHandler follower_updater_handler;
	
	public ServerMain(String settings_file)
	{
		settings = getServerSettings(settings_file);
		InetSocketAddress server_address = getServerAddress();
		MessageUtils.registerJsonDeserializers();
		
		server = new WinsomeServerImpl(settings);		
		autosaver = new ServerAutosaver(server);
		registrator_handler = new RegistratorRMIHandler(server.getWinsomeData(), server.getThreadpool());
		client_handler = new ClientHandler(server_address, server.getWinsomeData(), server.getThreadpool());
		follower_updater_handler = new FollowerUpdaterRegistratorHandler();
	}
	
	public void startServer() throws IOException, AlreadyBoundException
	{
		ServerRMIRegistry.startRegistry();
		server.startServer();
		
		autosaver.startAutosaver();
		registrator_handler.bindObject();
		client_handler.startClientHandler();
		follower_updater_handler.bindObject();
	}
	
	public void stopServer() throws InterruptedException, IOException, NotBoundException
	{
		follower_updater_handler.unbindObject();
		client_handler.stopClientHandler();
		registrator_handler.unbindObject();
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
			return new InetSocketAddress(InetAddress.getByName(settings.server_address), WinsomeConnectionProtocol.getTCPListenerPort());
		}
		catch (UnknownHostException e) { throw new RuntimeException(e.getMessage()); }
	}
}

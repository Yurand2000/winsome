package winsome.server_app;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.registrator.RegistratorRMIHandler;
import winsome.server_app.internal.*;

public class ServerMain
{
	private final WinsomeServer server;
	private final ServerAutosaver autosaver;
	private final ClientHandler client_handler;
	private final RegistratorRMIHandler registrator_handler;
	private final FollowerUpdaterRegistratorHandler follower_updater_handler;
	
	public ServerMain(InetSocketAddress address, String savefile)
	{
		server = new WinsomeServerImpl(savefile);
		autosaver = new ServerAutosaver(server);
		client_handler = new ClientHandler(address, server);
		registrator_handler = new RegistratorRMIHandler(server);
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
}

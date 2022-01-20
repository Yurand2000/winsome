package winsome.server_app;

import java.io.IOException;
import java.net.InetSocketAddress;

import winsome.server_app.internal.*;

public class ServerMain
{
	private final WinsomeServer server;
	private final ServerAutosaver autosaver;
	private final ClientHandler client_handler;
	
	public ServerMain(InetSocketAddress address, String savefile)
	{
		server = new WinsomeServerImpl(savefile);
		autosaver = new ServerAutosaver(server);
		client_handler = new ClientHandler(address, server);
	}
	
	public void startServer() throws IOException
	{
		server.startServer();
		autosaver.startAutosaver();
		client_handler.startClientHandler();
	}
	
	public void stopServer() throws InterruptedException, IOException
	{
		client_handler.stopClientHandler();
		autosaver.stopAutosaver();
		server.shutdownServer();
	}
}

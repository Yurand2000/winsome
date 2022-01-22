package winsome.server_app;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

public class ServerAppMain
{
	private static ServerMain server;
	private static String settings_file = "settings.json";
	
	public static void main(String[] args) throws IOException, InterruptedException, AlreadyBoundException, NotBoundException
	{		
		System.out.println("Server starting...");
		System.out.println("* Server settings file: " + settings_file);
		
		server = new ServerMain(settings_file);
		System.out.println("* Server address: " + server.settings.server_address);
		System.out.println("* Server multicast address: " + server.settings.server_udp_address);
		System.out.println("* Server save file: " + server.settings.save_file);
		
		server.startServer();

		System.out.println("Server started. Press ENTER to stop.");
		System.in.read();
		
		server.stopServer();
		System.out.println("Server stopped. Quitting...");
	}
}

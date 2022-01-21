package winsome.server_app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

public class ServerAppMain
{
	private static ServerMain server;
	private static InetSocketAddress address;
	private static String savefile;
	
	public static void main(String[] args) throws IOException, InterruptedException, AlreadyBoundException, NotBoundException
	{
		savefile = "savefile.json";
		address = new InetSocketAddress(InetAddress.getByName("localhost"), 8080);
		
		System.out.println("Server starting...");
		System.out.println("* Server savefile: " + savefile);
		System.out.println("* Server address: " + address.toString());
		
		server = new ServerMain(address, savefile);
		server.startServer();

		System.out.println("Server started. Press ENTER to stop.");
		System.in.read();
		
		server.stopServer();
		System.out.println("Server stopped. Quitting...");
	}
}

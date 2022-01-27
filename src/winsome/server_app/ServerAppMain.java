package winsome.server_app;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import winsome.server_app.internal.ServerSettings;

public class ServerAppMain
{
	private static ServerMain server;
	private static String settings_file = "settings.json";
	
	public static void main(String[] args) throws IOException, InterruptedException, AlreadyBoundException, NotBoundException
	{		
		System.out.println("Winsome Server starting...");
		
		server = new ServerMain(settings_file);
		printCurrentSettings(server.settings);
		
		server.startServer();

		System.out.println("Winsome Server started. Press ENTER to stop.");
		System.in.read();
		
		server.stopServer();
		System.out.println("Winsome Server stopped. Quitting...");
	}
	
	private static void printCurrentSettings(ServerSettings settings)
	{
		System.out.println("Server settings: ");
		System.out.println("* Server settings file: " + settings_file);
		System.out.println("* Server save file: " + settings.save_file);
		System.out.println("*");
		System.out.println("* Server address: " + settings.server_address + ":" + settings.server_port);
		System.out.println("* Server RMI registry address: " + settings.server_address + ":" + settings.server_rmi_port);
		System.out.println("* Server multicast address: " + settings.server_udp_address + ":" + settings.server_udp_port);
		System.out.println("*");
		System.out.println("* Server reward timer (in seconds): " + settings.reward_timer_in_seconds);
		System.out.println("* Server author / curator reward: " + settings.reward_author_part + " / " +
			new BigDecimal(1).subtract(new BigDecimal(settings.reward_author_part)).toPlainString());
	}
}

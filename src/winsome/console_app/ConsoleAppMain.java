package winsome.console_app;

import java.util.List;

import winsome.client_app.ClientAppAPI;
import winsome.connection.protocols.WinsomeConnectionProtocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ConsoleAppMain
{
	public static void main(String[] args) throws InterruptedException
	{
		ClientAppAPI.startClient(getServerAddress(getHostname(args)));

		ConsoleExecutor.instance().setExecutorChain(generateExecutorChain());
		ConsoleExecutor.instance().startConsoleExecutor();
		System.out.println("Winsome Console Client Started.");
		
		ConsoleExecutor.instance().joinConsoleExecutor();
		System.out.println("Winsome Console Client Closed.");
	}
	
	private static InetSocketAddress getServerAddress(String hostname)
	{
		return InetSocketAddress.createUnresolved(hostname, WinsomeConnectionProtocol.getTCPListenerPort());
	}
	
	private static List<Class<? extends ConsoleCommandExecutor>> generateExecutorChain()
	{
		List<Class<? extends ConsoleCommandExecutor>> chain_executors = new ArrayList<Class<? extends ConsoleCommandExecutor>>();
		chain_executors.add(AnyCommandExecutor.class);
		chain_executors.add(ExitCommandExecutor.class);
		
		return chain_executors;
	}
	
	private static String getHostname(String[] args)
	{
		if(args.length > 0)
		{
			return args[0];
		}
		else
		{
			return "0.0.0.0";
		}
	}
}

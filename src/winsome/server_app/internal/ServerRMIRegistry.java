package winsome.server_app.internal;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import winsome.connection.protocols.WinsomeConnectionProtocol;

public class ServerRMIRegistry
{
	private ServerRMIRegistry() { }
	
	public static void startRegistry() throws IOException
	{
		LocateRegistry.createRegistry( WinsomeConnectionProtocol.getRMIRegistryPort() );
		//after this call, the registry port will be permanently bound to a socket until program shutdown
	}
	
	public static void shutdownRegistry() throws IOException
	{
		UnicastRemoteObject.unexportObject(LocateRegistry.getRegistry( WinsomeConnectionProtocol.getRMIRegistryPort() ), true);
	}
}
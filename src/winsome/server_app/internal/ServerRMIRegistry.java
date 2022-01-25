package winsome.server_app.internal;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMIRegistry
{
	private static Registry registry = null;
	
	private ServerRMIRegistry() { }
	
	public static void startRegistry(Integer port) throws IOException
	{
		registry = LocateRegistry.createRegistry( port );
		//after this call, the registry port will be permanently bound to a socket until program shutdown
	}
	
	public static void shutdownRegistry() throws IOException
	{
		UnicastRemoteObject.unexportObject(registry, true);
	}
}

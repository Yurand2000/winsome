package winsome.connection.client_api;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIObjectLookup
{
	private RMIObjectLookup() { }

	public static <T extends Remote> T getStub(String hostname, Integer port, Class<T> obj_class, String name) throws IOException, NotBoundException
	{
		return obj_class.cast( getRegistry(hostname, port).lookup(name) );
	}
	
	public static <T extends Remote> T generateStub(Class<T> obj_class, T object) throws RemoteException
	{
		return obj_class.cast( UnicastRemoteObject.exportObject(object, 0) );
	}
	
	public static <T extends Remote> void destroyStub(T object) throws RemoteException
	{
		UnicastRemoteObject.unexportObject(object, true);
	}
	
	private static Registry getRegistry(String hostname, Integer port) throws RemoteException
	{
		return LocateRegistry.getRegistry(hostname, port);
	}
}

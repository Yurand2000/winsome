package winsome.connection.server_api;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIObjectRegistrator<T extends Remote>
{
	protected final T object;
	private final String object_name;
	private final Integer registry_port;
	
	public RMIObjectRegistrator(T object, String name, Integer registry_port)
	{
		this.object = object;
		this.object_name = name;
		this.registry_port = registry_port;
	}
	
	public void bindObject() throws IOException, AlreadyBoundException
	{
        getRegistry().bind( object_name, generateStub() );
	}
	
	public void unbindObject() throws IOException, NotBoundException
	{
		getRegistry().unbind( object_name );
	}
	
	private Remote generateStub() throws RemoteException
	{
		return UnicastRemoteObject.exportObject( object, registry_port );
	}
	
	private Registry getRegistry() throws RemoteException
	{
		return LocateRegistry.getRegistry( registry_port );
	}
}

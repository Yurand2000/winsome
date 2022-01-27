package winsome.connection.client_api.follower_updater;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import winsome.connection.client_api.RMIObjectLookup;
import winsome.connection.protocols.FollowerUpdaterRMI;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistrator;

public class FollowerUpdaterRMIHandlerImpl implements FollowerUpdaterRMIHandler
{
	private final String hostname;
	private final Integer port;
	private final String user;
	private final Set<String> followers;
	private FollowerUpdaterImpl updater;
	private FollowerUpdaterRegistrator registrator;
	
	public FollowerUpdaterRMIHandlerImpl(String hostname, Integer port, String user, Set<String> followers)
	{
		this.hostname = hostname;
		this.port = port;
		this.user = user;
		this.followers = followers;
		
		updater = null;
		registrator = null;
	}
	
	public void registerFollowerUpdater() throws RemoteException
	{		
		if(registrator == null)
		{
			try
			{
				tryRegister();
			}
			catch (NotBoundException e)
			{
				destroy();
				throw new RuntimeException(e.toString());
			}
			catch(RemoteException e)
			{
				destroy();
				throw e;
			}
		}
	}
	
	private void tryRegister() throws RemoteException, NotBoundException
	{
		registrator = RMIObjectLookup.getStub(hostname, port, FollowerUpdaterRegistrator.class, FollowerUpdaterRMI.getFollowerUpdaterRegistratorName() );
		updater = new FollowerUpdaterImpl(user, followers);
		registrator.registerFollowerUpdater(updater);
	}
	
	public void unregisterFollowerUpdater()
	{
		try
		{
			if(registrator != null)
			{
				registrator.unregisterFollowerUpdater(updater);
				destroy();
			}
		}
		catch(RemoteException e) { }
	}
	
	private void destroy()
	{
		try { UnicastRemoteObject.unexportObject(updater, true); }
		catch (NoSuchObjectException e) { }
		updater = null;
		registrator = null;
	}
}

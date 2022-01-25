package winsome.connection.client_api.follower_updater;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

import winsome.connection.client_api.RMIObjectLookup;
import winsome.connection.protocols.FollowerUpdaterRMI;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistrator;

public class FollowerUpdaterRMIHandlerImpl implements FollowerUpdaterRMIHandler
{
	private final String hostname;
	private final Integer port;
	private final FollowerUpdaterImpl updater;
	private FollowerUpdaterRegistrator registrator;
	private FollowerUpdater stub;
	
	public FollowerUpdaterRMIHandlerImpl(String hostname, Integer port, String user, Set<String> followers) throws IOException, NotBoundException
	{
		this.hostname = hostname;
		this.port = port;
		updater = new FollowerUpdaterImpl(user, followers);
		
		registrator = null;
		stub = null;
	}
	
	public void registerFollowerUpdater() throws RemoteException
	{
		createStub();
		
		if(registrator == null)
		{
			try
			{
				registrator = RMIObjectLookup.getStub(hostname, port, FollowerUpdaterRegistrator.class, FollowerUpdaterRMI.getFollowerUpdaterRegistratorName() );
				registrator.registerFollowerUpdater(stub);
			}
			catch (NotBoundException | IOException e)
			{
				destroyStub();
				registrator = null;
			}
		}
	}
	
	public void unregisterFollowerUpdater() throws RemoteException
	{
		try
		{
			if(registrator != null)
			{
				registrator.unregisterFollowerUpdater(stub);
				registrator = null;
			}
		}
		catch(RemoteException e) { }
		finally
		{
			destroyStub();
		}
	}
	
	private void createStub() throws RemoteException
	{
		if(stub == null)
		{
			stub = RMIObjectLookup.generateStub(FollowerUpdater.class, updater);
		}
	}
	
	private void destroyStub() throws RemoteException
	{
		if(stub != null)
		{
			RMIObjectLookup.destroyStub(updater);
			stub = null;
		}
	}
}

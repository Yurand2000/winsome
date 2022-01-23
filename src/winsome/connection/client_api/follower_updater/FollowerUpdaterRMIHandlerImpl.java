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
	private final FollowerUpdaterRegistrator registrator;
	private FollowerUpdaterImpl updater;
	private FollowerUpdater stub;
	
	public FollowerUpdaterRMIHandlerImpl(String hostname, String user, Set<String> followers) throws IOException, NotBoundException
	{
		registrator = RMIObjectLookup.getStub(hostname, FollowerUpdaterRegistrator.class, FollowerUpdaterRMI.getFollowerUpdaterRegistratorName() );
		updater = new FollowerUpdaterImpl(user, followers);
		stub = RMIObjectLookup.generateStub(FollowerUpdater.class, updater);
	}
	
	public void registerFollowerUpdater() throws RemoteException
	{
		registrator.registerFollowerUpdater(stub);
	}
	
	public void unregisterFollowerUpdater() throws RemoteException
	{
		registrator.unregisterFollowerUpdater(stub);
	}
}

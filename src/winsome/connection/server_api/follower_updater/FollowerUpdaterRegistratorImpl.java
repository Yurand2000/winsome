package winsome.connection.server_api.follower_updater;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.HashMap;
import java.util.Map;

import winsome.connection.client_api.follower_updater.FollowerUpdater;

public class FollowerUpdaterRegistratorImpl extends RemoteObject implements FollowerUpdaterRegistrator
{
	private static final long serialVersionUID = 1L;
	
	private final Map<String, FollowerUpdater> callback_updaters;
	
	public FollowerUpdaterRegistratorImpl()
	{
		callback_updaters = new HashMap<String, FollowerUpdater>();
	}
	
	public void notifyNewFollower(String user, String new_follower)
	{
		try
		{
			FollowerUpdater updater = callback_updaters.get(user);
			if(updater != null)
				updater.notifyNewFollowing(new_follower);
		}
		catch (RemoteException e) { }
	}
	
	public void notifyRemovedFollower(String user, String removed_follower)
	{
		try
		{
			FollowerUpdater updater = callback_updaters.get(user);
			if(updater != null)
				updater.notifyRemovedFollowing(removed_follower);
		}
		catch (RemoteException e) { }
	}

	@Override
	public void registerFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException
	{
		callback_updaters.put(callback_updater.getUserToUpdate(), callback_updater);
	}

	@Override
	public void unregisterFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException
	{
		callback_updaters.remove(callback_updater.getUserToUpdate());
	}
}

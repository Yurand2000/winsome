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
	
	public synchronized void notifyNewFollower(String user, String new_follower)
	{
		try
		{
			FollowerUpdater updater = callback_updaters.get(user);
			if(updater != null)
				updater.notifyNewFollowing(new_follower);
		}
		catch (RemoteException e)
		{
			unregisterFollowerUpdater(user);
		}
	}
	
	public synchronized void notifyRemovedFollower(String user, String removed_follower)
	{
		try
		{
			FollowerUpdater updater = callback_updaters.get(user);
			if(updater != null)
				updater.notifyRemovedFollowing(removed_follower);
		}
		catch (RemoteException e)
		{
			unregisterFollowerUpdater(user);
		}
	}
	
	private void unregisterFollowerUpdater(String username)
	{
		callback_updaters.remove(username);
	}

	@Override
	public synchronized void registerFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException
	{
		String username = callback_updater.getUserToUpdate();
		callback_updaters.put(username, callback_updater);
	}

	@Override
	public synchronized void unregisterFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException
	{
		String username = null;
		try
		{
			username = callback_updater.getUserToUpdate();
		}
		catch (RemoteException e)
		{
			username = inverselyFindUserUpdater(callback_updater);
		}
		finally
		{
			unregisterFollowerUpdater(username);
		}
	}
	
	private String inverselyFindUserUpdater(FollowerUpdater callback_updater)
	{
		for(Map.Entry<String, FollowerUpdater> entry : callback_updaters.entrySet())
		{
			if(entry.hashCode() == callback_updater.hashCode())
			{
				return entry.getKey();
			}
		}		
		return null;
	}
}

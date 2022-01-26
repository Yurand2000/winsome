package winsome.connection.client_api.follower_updater;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class FollowerUpdaterImpl extends UnicastRemoteObject implements FollowerUpdater
{
	private static final long serialVersionUID = 1L;
	
	private final String user;
	private final Set<String> followers;
	
	public FollowerUpdaterImpl(String me, Set<String> followers) throws RemoteException
	{
		user = me;
		this.followers = followers;
	}

	@Override
	public void notifyNewFollowing(String username) throws RemoteException
	{
		followers.add(username);
	}

	@Override
	public void notifyRemovedFollowing(String username) throws RemoteException
	{
		followers.remove(username);
	}

	@Override
	public String getUserToUpdate() throws RemoteException
	{
		return user;
	}

}

package winsome.connection.client_api.follower_updater;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FollowerUpdater extends Remote
{
	public void notifyNewFollowing(String username) throws RemoteException;
	public void notifyRemovedFollowing(String username) throws RemoteException;
	public String getUserToUpdate() throws RemoteException;
}

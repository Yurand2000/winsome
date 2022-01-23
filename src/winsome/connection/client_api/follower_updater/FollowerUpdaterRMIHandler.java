package winsome.connection.client_api.follower_updater;

import java.rmi.RemoteException;

public interface FollowerUpdaterRMIHandler
{
	void registerFollowerUpdater() throws RemoteException;
	void unregisterFollowerUpdater() throws RemoteException;
}

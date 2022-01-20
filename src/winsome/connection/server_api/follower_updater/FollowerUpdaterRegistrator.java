package winsome.connection.server_api.follower_updater;

import java.rmi.Remote;
import java.rmi.RemoteException;

import winsome.connection.client_api.follower_updater.FollowerUpdater;

public interface FollowerUpdaterRegistrator extends Remote
{
	public void registerFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException;
	public void unregisterFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException;
}

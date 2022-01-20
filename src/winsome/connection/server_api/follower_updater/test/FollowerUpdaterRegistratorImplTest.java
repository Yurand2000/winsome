package winsome.connection.server_api.follower_updater.test;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorImpl;

class FollowerUpdaterRegistratorImplTest
{
	@Test
	void testRegisterFollowerUpdater() throws RemoteException
	{
		FollowerUpdaterTest updater = new FollowerUpdaterTest();
		FollowerUpdaterRegistratorImpl registrator = new FollowerUpdaterRegistratorImpl();
		
		registrator.registerFollowerUpdater(updater);
		updater.checkGetUserCalled();
	}
	
	@Test
	void testUnregisterFollowerUpdater() throws RemoteException
	{
		FollowerUpdaterTest updater = new FollowerUpdaterTest();
		FollowerUpdaterRegistratorImpl registrator = new FollowerUpdaterRegistratorImpl();
		
		registrator.unregisterFollowerUpdater(updater);
		updater.checkGetUserCalled();
	}
	
	@Test
	void testNotifyNewFollower() throws RemoteException
	{
		FollowerUpdaterTest updater = new FollowerUpdaterTest();
		FollowerUpdaterRegistratorImpl registrator = new FollowerUpdaterRegistratorImpl();
		
		registrator.registerFollowerUpdater(updater);
		
		updater.setExpectedUsername("Asia");
		registrator.notifyNewFollower("Stefanie", "Asia");
		updater.checkNewFollowingCalled();

		registrator.unregisterFollowerUpdater(updater);
		
		registrator.notifyNewFollower("Stefanie", "Asia");
		updater.checkNewFollowingNotCalled();
	}
	
	@Test
	void testNotifyRemovedFollower() throws RemoteException
	{
		FollowerUpdaterTest updater = new FollowerUpdaterTest();
		FollowerUpdaterRegistratorImpl registrator = new FollowerUpdaterRegistratorImpl();

		registrator.registerFollowerUpdater(updater);
		
		updater.setExpectedUsername("Asia");
		registrator.notifyRemovedFollower("Stefanie", "Asia");
		updater.checkRemovedFollowingCalled();

		registrator.unregisterFollowerUpdater(updater);
		
		registrator.notifyRemovedFollower("Stefanie", "Asia");
		updater.checkRemovedFollowingNotCalled();
	}
}

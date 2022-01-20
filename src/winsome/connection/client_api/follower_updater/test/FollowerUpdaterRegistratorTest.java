package winsome.connection.client_api.follower_updater.test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import winsome.connection.client_api.follower_updater.FollowerUpdater;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistrator;

public class FollowerUpdaterRegistratorTest implements FollowerUpdaterRegistrator
{
	private boolean called_register = false;
	private boolean called_unregister = false;
	private FollowerUpdater updater = null;
	
	public FollowerUpdaterRegistratorTest() { }

	@Override
	public void registerFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException
	{
		updater = callback_updater;
		assertEquals(updater.getUserToUpdate(), "Luigi");
		called_register = true;
	}

	@Override
	public void unregisterFollowerUpdater(FollowerUpdater callback_updater) throws RemoteException
	{
		assertEquals(updater.getUserToUpdate(), callback_updater.getUserToUpdate());
		assertEquals(updater.getUserToUpdate(), "Luigi");
		called_unregister = true;
	}
	
	public void checkCalledRegister()
	{
		assertTrue(called_register);
		called_register = false;
	}
	
	public void checkCalledUnregister()
	{
		assertTrue(called_unregister);
		called_unregister = false;
	}
}

package winsome.connection.client_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandler;

class FollowerUpdaterRMIHandlerTest implements FollowerUpdaterRMIHandler
{
	private boolean register_called = false;
	private boolean unregister_called = false;
	
	public FollowerUpdaterRMIHandlerTest()
	{
		
	}

	@Override
	public void registerFollowerUpdater() throws RemoteException
	{
		register_called = true;
	}
	
	public void checkRegisterCalled()
	{
		assertTrue(register_called);
		register_called = false;
	}

	@Override
	public void unregisterFollowerUpdater() throws RemoteException
	{
		unregister_called = true;
	}
	
	public void checkUnregisterCalled()
	{
		assertTrue(unregister_called);
		unregister_called = false;
	}

}

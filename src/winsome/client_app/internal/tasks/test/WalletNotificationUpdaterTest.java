package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.connection.client_api.wallet_notifier.WalletNotificationUpdater;

class WalletNotificationUpdaterTest implements WalletNotificationUpdater
{
	private String address = null;
	private Runnable task = null;
	private boolean register_called = false;
	private boolean unregister_called = false;
	
	public WalletNotificationUpdaterTest()
	{
		
	}

	public void setExpectedArguments(String address, Runnable task)
	{
		this.address = address;
		this.task = task;
	}
	
	@Override
	public void registerWalletUpdateNotifications(String address, Runnable task)
	{
		assertEquals(address, this.address);
		assertEquals(task, this.task);
		register_called = true;
	}
	
	public void checkRegisterCalled()
	{
		assertTrue(register_called);
		register_called = false;
	}

	@Override
	public void unregisterWalletUpdateNotifications()
	{
		unregister_called = true;
	}
	
	public void checkUnregisterCalled()
	{
		assertTrue(unregister_called);
		unregister_called = false;
	}
	
}

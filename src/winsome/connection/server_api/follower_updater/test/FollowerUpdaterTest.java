package winsome.connection.server_api.follower_updater.test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

import winsome.connection.client_api.follower_updater.FollowerUpdater;

public class FollowerUpdaterTest extends RemoteObject implements FollowerUpdater
{
	private static final long serialVersionUID = 1L;
	
	private boolean get_user_called = false;
	private boolean new_following_called = false;
	private boolean removed_following_called = false;
	private String expected_username = null;
	
	public FollowerUpdaterTest() { }

	@Override
	public void notifyNewFollowing(String username) throws RemoteException
	{
		assertEquals(username, expected_username);
		expected_username = null;
		new_following_called = true;
	}

	@Override
	public void notifyRemovedFollowing(String username) throws RemoteException
	{
		assertEquals(username, expected_username);
		expected_username = null;
		removed_following_called = true;
	}

	@Override
	public String getUserToUpdate() throws RemoteException
	{
		get_user_called = true;
		return "Stefanie";
	}
	
	public void checkNewFollowingCalled()
	{
		assertTrue(new_following_called);
		new_following_called = false;
	}
	
	public void checkNewFollowingNotCalled()
	{
		assertFalse(new_following_called);
	}
	
	public void checkRemovedFollowingCalled()
	{
		assertTrue(removed_following_called);
		removed_following_called = false;
	}
	
	public void checkRemovedFollowingNotCalled()
	{
		assertFalse(removed_following_called);
	}
	
	public void checkGetUserCalled()
	{
		assertTrue(get_user_called);
		get_user_called = false;
	}
	
	public void setExpectedUsername(String username)
	{
		expected_username = username;
	}
}

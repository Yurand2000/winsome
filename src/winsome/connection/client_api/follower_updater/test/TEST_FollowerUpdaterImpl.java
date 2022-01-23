package winsome.connection.client_api.follower_updater.test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import winsome.connection.client_api.follower_updater.FollowerUpdaterImpl;

class TEST_FollowerUpdaterImpl
{
	@Test
	void testGetUserToUpdate() throws RemoteException
	{
		Set<String> followerSet = Collections.synchronizedSet( new HashSet<String>() );
		FollowerUpdaterImpl updater = new FollowerUpdaterImpl("Luigi", followerSet);
		
		assertEquals(updater.getUserToUpdate(), "Luigi");
	}
	
	@Test
	void testNotifyNewFollowing() throws RemoteException
	{
		Set<String> followerSet = Collections.synchronizedSet( new HashSet<String>() );
		FollowerUpdaterImpl updater = new FollowerUpdaterImpl("Luigi", followerSet);
		
		assertTrue(followerSet.isEmpty());
		
		updater.notifyNewFollowing("Nicola");
		assertFalse(followerSet.isEmpty());
		assertTrue(followerSet.contains("Nicola"));
		assertFalse(followerSet.contains("Luca"));
		
		updater.notifyNewFollowing("Luca");
		assertTrue(followerSet.contains("Nicola"));
		assertTrue(followerSet.contains("Luca"));
	}

	@Test
	void testNotifyRemovedFollowing() throws RemoteException
	{
		Set<String> followerSet = Collections.synchronizedSet( new HashSet<String>() );
		FollowerUpdaterImpl updater = new FollowerUpdaterImpl("Luigi", followerSet);		
		updater.notifyNewFollowing("Nicola");
		updater.notifyNewFollowing("Luca");

		assertTrue(followerSet.contains("Nicola"));
		assertTrue(followerSet.contains("Luca"));
		
		updater.notifyRemovedFollowing("Luca");
		assertTrue(followerSet.contains("Nicola"));
		assertFalse(followerSet.contains("Luca"));
		
		updater.notifyRemovedFollowing("Nicola");
		assertTrue(followerSet.isEmpty());
	}
}

package winsome.connection.bridge_test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.server_api.follower_updater.*;
import winsome.connection.client_api.follower_updater.*;
import winsome.server_app.internal.ServerRMIRegistry;

class TEST_FollowerUpdater
{
	private FollowerUpdaterRegistratorHandlerImpl server_handler;
	private FollowerUpdaterRMIHandler client_handler;
	private Set<String> followers;
	
	@BeforeEach
	void setup() throws IOException, AlreadyBoundException, NotBoundException
	{
		ServerRMIRegistry.startRegistry("localhost", 8081);
		server_handler = new FollowerUpdaterRegistratorHandlerImpl(8081);
		server_handler.bindObject();
		
		followers = new HashSet<String>();
		client_handler = new FollowerUpdaterRMIHandlerImpl("localhost", 8081, "Luca", followers);
		client_handler.registerFollowerUpdater();
	}
	
	@Test
	void test()
	{
		assertTrue(followers.isEmpty());
		
		server_handler.notifyNewFollower("Luca", "Luigi");
		assertTrue(followers.contains("Luigi"));
		
		server_handler.notifyRemovedFollower("Luca", "Luigi");
		assertFalse(followers.contains("Luigi"));
		assertTrue(followers.isEmpty());		
	}

	@AfterEach
	void teardown() throws IOException, NotBoundException
	{
		client_handler.unregisterFollowerUpdater();
		
		server_handler.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
}

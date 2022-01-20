package winsome.connection.server_api.follower_updater.test;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.client_api.RMIObjectLookup;
import winsome.connection.client_api.follower_updater.FollowerUpdater;
import winsome.connection.protocols.FollowerUpdaterRMI;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistrator;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.server_app.internal.ServerRMIRegistry;

class FollowerUpdaterRegistratorHandlerTest
{
	private FollowerUpdaterRegistratorHandler handler;
	private FollowerUpdaterRegistrator registrator;
	private FollowerUpdaterTest updater;
	
	@BeforeEach
	void setup() throws IOException, AlreadyBoundException
	{
		ServerRMIRegistry.startRegistry();
		handler = new FollowerUpdaterRegistratorHandler();
		handler.bindObject();

		}

	@Test
	void testNotifyNewFollower() throws IOException, NotBoundException
	{
		registrator = RMIObjectLookup.getStub("localhost", FollowerUpdaterRegistrator.class, FollowerUpdaterRMI.getFollowerUpdaterRegistratorName());
		updater = new FollowerUpdaterTest();
		FollowerUpdater stub = RMIObjectLookup.generateStub(FollowerUpdater.class, updater);
		
		registrator.registerFollowerUpdater(stub);
		updater.setExpectedUsername("Luke");
		handler.notifyNewFollower("Stefanie", "Luke");
		updater.checkNewFollowingCalled();
	}

	@Test
	void testNotifyRemovedFollower() throws IOException, NotBoundException
	{
		registrator = RMIObjectLookup.getStub("localhost", FollowerUpdaterRegistrator.class, FollowerUpdaterRMI.getFollowerUpdaterRegistratorName());
		updater = new FollowerUpdaterTest();
		FollowerUpdater stub = RMIObjectLookup.generateStub(FollowerUpdater.class, updater);
		
		registrator.registerFollowerUpdater(stub);
		updater.setExpectedUsername("Luke");
		handler.notifyRemovedFollower("Stefanie", "Luke");
		updater.checkRemovedFollowingCalled();
	}
	
	@AfterEach
	void stopRegister() throws IOException, NotBoundException
	{
		handler.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
}

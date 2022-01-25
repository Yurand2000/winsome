package winsome.connection.client_api.follower_updater.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandler;
import winsome.connection.client_api.follower_updater.FollowerUpdaterRMIHandlerImpl;
import winsome.connection.protocols.FollowerUpdaterRMI;
import winsome.connection.server_api.RMIObjectRegistrator;
import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistrator;
import winsome.server_app.internal.ServerRMIRegistry;

class TEST_FollowerUpdaterRMIHandlerImplTest
{
	private FollowerUpdaterRegistratorTest registratorImpl = null;
	private RMIObjectRegistrator<FollowerUpdaterRegistrator> registrator = null;
	
	@BeforeEach
	void startRegister() throws IOException, AlreadyBoundException
	{
		registratorImpl = new FollowerUpdaterRegistratorTest();
		ServerRMIRegistry.startRegistry(8081);
		registrator = new RMIObjectRegistrator<FollowerUpdaterRegistrator>(
				registratorImpl,
				FollowerUpdaterRMI.getFollowerUpdaterRegistratorName(),
				8081
			);
		registrator.bindObject();
	}

	@Test
	@SuppressWarnings("unused")
	void testConstructor() throws IOException, NotBoundException
	{
		assertDoesNotThrow(() -> {
			FollowerUpdaterRMIHandler handler = new FollowerUpdaterRMIHandlerImpl("localhost", 8081, "Luigi", null);
		});
	}
	
	@Test
	void testRegisterFollowerUpdater() throws IOException, NotBoundException
	{
		FollowerUpdaterRMIHandler handler = new FollowerUpdaterRMIHandlerImpl("localhost", 8081, "Luigi", null);
		
		handler.registerFollowerUpdater();
		registratorImpl.checkCalledRegister();
	}

	@Test
	void testUnregisterFollowerUpdater() throws IOException, NotBoundException, RemoteException
	{
		FollowerUpdaterRMIHandler handler = new FollowerUpdaterRMIHandlerImpl("localhost", 8081, "Luigi", null);
		handler.registerFollowerUpdater();
		
		handler.unregisterFollowerUpdater();
		registratorImpl.checkCalledUnregister();
	}
	
	@AfterEach
	void teardownRegister() throws IOException, NotBoundException
	{
		registrator.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
}

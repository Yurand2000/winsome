package winsome.connection.bridge_test;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.client_api.registrator.RegistratorRMIHandler;
import winsome.connection.server_api.registrator.test.WinsomeServerTest;
import winsome.server_app.internal.ServerRMIRegistry;

class RegistratorTest
{
	private WinsomeServerTest server;	
	private winsome.connection.server_api.registrator.RegistratorRMIHandler server_registrator;

	@BeforeEach
	void setup() throws IOException, AlreadyBoundException
	{
		server = new WinsomeServerTest();
		ServerRMIRegistry.startRegistry();
		server_registrator = new winsome.connection.server_api.registrator.RegistratorRMIHandler(server);
		server_registrator.bindObject();
	}
	
	@Test
	void testRegistration()
	{
		RegistratorRMIHandler.register("localhost", "Benito", "password", new String[] {"Auto"});
		server.checkExecuteTaskNowCalled();
	}

	@AfterEach
	void teardown() throws IOException, NotBoundException
	{
		server_registrator.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
}

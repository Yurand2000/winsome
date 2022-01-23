package winsome.connection.bridge_test;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.client_api.registrator.RegistratorRMIHandler;
import winsome.server_app.internal.ServerRMIRegistry;

class TEST_RegistratorTest
{
	private WinsomeDataTest data = null;
	private ServerThreadpoolTest pool = null;
	private winsome.connection.server_api.registrator.RegistratorRMIHandler server_registrator;

	@BeforeEach
	void setup() throws IOException, AlreadyBoundException
	{
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		ServerRMIRegistry.startRegistry();
		server_registrator = new winsome.connection.server_api.registrator.RegistratorRMIHandler(data, pool);
		server_registrator.bindObject();
	}
	
	@Test
	void testRegistration()
	{
		RegistratorRMIHandler.register("localhost", "Benito", "password", new String[] {"Auto"});
		pool.checkEnqueueCalled();
	}

	@AfterEach
	void teardown() throws IOException, NotBoundException
	{
		server_registrator.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
}

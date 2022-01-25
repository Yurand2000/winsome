package winsome.connection.server_api.registrator.test;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.client_api.RMIObjectLookup;
import winsome.connection.protocols.RegistratorRMI;
import winsome.connection.server_api.registrator.Registrator;
import winsome.connection.server_api.registrator.RegistratorRMIHandler;
import winsome.server_app.internal.ServerRMIRegistry;

class TEST_RegistratorRMIHandler 
{
	private WinsomeDataTest data = null;
	private ServerThreadpoolTest pool = null;
	private RegistratorRMIHandler registrator = null;
	
	@BeforeEach
	void setup() throws IOException, AlreadyBoundException
	{
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		ServerRMIRegistry.startRegistry(8081);
		registrator = new RegistratorRMIHandler(data, pool, 8081);
		registrator.bindObject();		
	}
	
	@Test
	void testProxyCallsOriginalObject() throws IOException, NotBoundException
	{
		Registrator registrator = RMIObjectLookup.getStub("localhost", 8081, Registrator.class, RegistratorRMI.getRegistratorName());

		registrator.register("", "", new String[0]);
		
		pool.checkEnqueueCalled();
	}
	
	@AfterEach
	void teardown() throws IOException, NotBoundException
	{
		registrator.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
}

package winsome.connection.client_api.registrator.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.client_api.registrator.RegistratorRMIHandler;
import winsome.connection.protocols.RegistratorRMI;
import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.connection.server_api.RMIObjectRegistrator;
import winsome.connection.server_api.registrator.Registrator;
import winsome.server_app.internal.ServerRMIRegistry;

class RegistratorRMIHandlerTest
{
	private RegistratorTestImpl registratorImpl = null;
	private RMIObjectRegistrator<Registrator> registrator = null;

	@BeforeEach
	void setup()
	{
		registratorImpl = new RegistratorTestImpl();
	}
	
	void startRegister() throws IOException, AlreadyBoundException
	{
		ServerRMIRegistry.startRegistry();
		registrator = new RMIObjectRegistrator<Registrator>(
			registratorImpl,
			RegistratorRMI.getRegistratorName(),
			WinsomeConnectionProtocol.getRMIRegistryPort()
		);
		registrator.bindObject();
	}
	
	void stopRegister() throws IOException, NotBoundException
	{
		registrator.unbindObject();
		ServerRMIRegistry.shutdownRegistry();
	}
	
	@Test
	void testTryRegister() throws IOException, AlreadyBoundException, NotBoundException
	{
		startRegister();
		
		registratorImpl.setExpectedValues("username", "pass", new String[] {"ciao", "ciao2"});
		RegistratorRMIHandler.register("localhost", "username", "pass", new String[] {"ciao", "ciao2"});
		assertTrue(registratorImpl.wasRegisterCalled());
		
		stopRegister();
	}

	@Test
	void testTryRegisterNotBound()
	{
		registratorImpl.setExpectedValues("username", "pass", new String[] {"ciao", "ciao2"});
		
		assertThrows(ServerInternalException.class, () -> {
			RegistratorRMIHandler.register("localhost", "username", "pass", new String[] {"ciao", "ciao2"});
		});
		assertFalse(registratorImpl.wasRegisterCalled());
	}
}

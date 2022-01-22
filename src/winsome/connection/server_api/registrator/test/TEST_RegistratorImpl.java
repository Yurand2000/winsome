package winsome.connection.server_api.registrator.test;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import winsome.connection.server_api.registrator.RegistratorImpl;

class TEST_RegistratorImpl
{
	private WinsomeServerTest server_test = null;
	
	@Test
	void testRegister() throws RemoteException
	{
		server_test = new WinsomeServerTest();
		
		RegistratorImpl reg = new RegistratorImpl(server_test);
		reg.register("", "", new String[0]);
		
		server_test.checkExecuteTaskNowCalled();
	}
}

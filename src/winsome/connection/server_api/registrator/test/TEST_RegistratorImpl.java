package winsome.connection.server_api.registrator.test;

import java.rmi.RemoteException;

import org.junit.jupiter.api.Test;

import winsome.connection.server_api.registrator.RegistratorImpl;

class TEST_RegistratorImpl
{
	private WinsomeDataTest data = null;
	private ServerThreadpoolTest pool = null;
	
	@Test
	void testRegister() throws RemoteException
	{
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		
		RegistratorImpl reg = new RegistratorImpl(data, pool);
		reg.register("", "", new String[0]);
		
		pool.checkEnqueueCalled();
	}
}

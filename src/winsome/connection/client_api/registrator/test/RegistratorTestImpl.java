package winsome.connection.client_api.registrator.test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.util.Arrays;

import winsome.connection.server_api.registrator.Registrator;

class RegistratorTestImpl implements Registrator
{
	private String expected_username;
	private String expected_password;
	private String[] expected_tags;
	private boolean register_called = false;
	
	public RegistratorTestImpl()
	{
		expected_username = null;
		expected_password = null;
		expected_tags = null;
	}
	
	@Override
	public void register(String username, String password, String[] tags) throws RemoteException
	{
		assertEquals(expected_username, username);
		assertEquals(expected_password, password);
		assertTrue(Arrays.equals(expected_tags, tags));
		register_called = true;
	}
	
	public void setExpectedValues(String username, String password, String[] tags)
	{
		expected_username = username;
		expected_password = password;
		expected_tags = Arrays.copyOf(tags, tags.length);
	}
	
	public boolean wasRegisterCalled()
	{
		boolean b = register_called;
		register_called = false;
		return b;
	}
}

package winsome.connection.client_api.registrator;

import java.io.IOException;
import java.rmi.NotBoundException;

import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.client_api.RMIObjectLookup;
import winsome.connection.protocols.RegistratorRMI;
import winsome.connection.server_api.registrator.Registrator;

public class RegistratorRMIHandler
{
	private RegistratorRMIHandler() { }
	
	public static void register(String hostname, String username, String password, String[] tags)
	{
		try 
		{
			RegistratorRMIHandler.tryRegister(hostname, username, password, tags);		
		}
		catch (IOException | NotBoundException e)
		{
			throw new ServerInternalException(e.getMessage());
		}
	}
	
	private static void tryRegister(String hostname, String username, String password, String[] tags) throws IOException, NotBoundException
	{
		Registrator registrator = RMIObjectLookup.getStub(hostname, Registrator.class, RegistratorRMI.getRegistratorName());
		registrator.register(username, password, tags);
	}
}

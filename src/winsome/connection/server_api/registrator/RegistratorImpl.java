package winsome.connection.server_api.registrator;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

import winsome.server_app.internal.WinsomeServer;
import winsome.server_app.internal.tasks.impl.RegisterNewUserTask;

public class RegistratorImpl  extends RemoteObject implements Registrator
{
	private static final long serialVersionUID = 1L;
	
	private WinsomeServer server;
	
	public RegistratorImpl(WinsomeServer server)
	{
		this.server = server;
	}
	
	public void register(String username, String password, String[] tags) throws RemoteException
	{
		server.executeTaskNow(new RegisterNewUserTask(username, password, tags));
	}
}

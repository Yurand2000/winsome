package winsome.connection.server_api.registrator;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.rmi.RegisterNewUserTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class RegistratorImpl  extends RemoteObject implements Registrator
{
	private static final long serialVersionUID = 1L;
	
	private WinsomeData winsome_data;
	private ServerThreadpool threadpool;
	
	public RegistratorImpl(WinsomeData winsome_data, ServerThreadpool threadpool)
	{
		this.winsome_data = winsome_data;
		this.threadpool = threadpool;
	}
	
	public void register(String username, String password, String[] tags) throws RemoteException
	{
		RegisterNewUserTask task = new RegisterNewUserTask(winsome_data, username, password, tags);
		threadpool.enqueueTask(task);
		
		try { task.get(); }
		catch (InterruptedException e) { throw new RuntimeException(e.toString()); }
	}
}

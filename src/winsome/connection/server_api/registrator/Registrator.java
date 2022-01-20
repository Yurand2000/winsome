package winsome.connection.server_api.registrator;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Registrator extends Remote
{
	public void register(String username, String password, String[] tags) throws RemoteException;
}

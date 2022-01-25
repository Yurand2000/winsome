package winsome.connection.server_api.socket;

import java.util.HashSet;
import java.util.Set;

import winsome.client_app.api.exceptions.AlreadyLoggedInException;
import winsome.client_app.api.exceptions.NotLoggedInException;

public class SocketStateCommonImpl implements SocketStateCommon
{
	Set<String> logged_users;
	
	public SocketStateCommonImpl()
	{
		logged_users = new HashSet<String>();
	}

	@Override
	public synchronized void setUserLoggedIn(String username)
	{
		if(logged_users.contains(username))
		{
			throw new AlreadyLoggedInException();
		}
		else
		{
			logged_users.add(username);
		}
	}

	@Override
	public synchronized void setUserLoggedOut(String username)
	{
		if(logged_users.contains(username))
		{
			logged_users.remove(username);
		}
		else
		{
			throw new NotLoggedInException();
		}
	}
}

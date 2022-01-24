package winsome.server_app.internal.tasks.impl;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public abstract class LoggedUserTask extends SocketClientTask
{
	public LoggedUserTask(SocketTaskState socket, WinsomeData data)
	{
		super(socket, data);
	}

	@Override
	protected final void execute(ServerThreadpool pool)
	{
		if(socket.getClientUser() != null)
		{
			executeTask(pool);
		}
		else
		{
			throw new RuntimeException("User not logged in.");
		}
	}
	
	protected abstract void executeTask(ServerThreadpool pool);
	
	protected User getCurrentUser()
	{
		return data.getUsers().get(socket.getClientUser());
	}
}

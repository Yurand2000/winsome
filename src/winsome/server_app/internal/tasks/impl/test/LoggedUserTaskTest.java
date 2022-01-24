package winsome.server_app.internal.tasks.impl.test;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.impl.LoggedUserTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

class LoggedUserTaskTest extends LoggedUserTask
{
	public boolean executed = false;
	public User user = null;

	public LoggedUserTaskTest(SocketTaskState socket, WinsomeData data)
	{
		super(socket, data);
	}

	@Override
	protected void executeTask(ServerThreadpool pool)
	{
		executed = true;
		user = getCurrentUser();
	}
}

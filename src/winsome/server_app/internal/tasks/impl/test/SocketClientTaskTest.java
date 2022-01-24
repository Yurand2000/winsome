package winsome.server_app.internal.tasks.impl.test;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.impl.SocketClientTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class SocketClientTaskTest extends SocketClientTask
{
	public boolean executed = false;
	public boolean execute_throws = false;

	public SocketClientTaskTest(SocketTaskState socket, WinsomeData data)
	{
		super(socket, data);
	}

	@Override
	protected void execute(ServerThreadpool pool)
	{
		executed = true;
		if(execute_throws)
		{
			throw new RuntimeException();
		}
	}
}

package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.WinsomeTask;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public abstract class SocketClientTask extends WinsomeTask
{
	protected final SocketTaskState socket;

	public SocketClientTask(SocketTaskState socket, WinsomeData data)
	{
		super(data);
		this.socket = socket;
	}
	
	@Override
	public final void run(ServerThreadpool pool)
	{
		try
		{
			execute(pool);
		}
		catch(RuntimeException e)
		{
			socket.sendAnswerMessage(new RequestExceptionAnswer(e.toString()));
		}
	}
	
	protected abstract void execute(ServerThreadpool pool);
}

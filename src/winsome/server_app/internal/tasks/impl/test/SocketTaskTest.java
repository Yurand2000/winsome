package winsome.server_app.internal.tasks.impl.test;

abstract class SocketTaskTest
{
	protected SocketTaskStateTest state;
	protected WinsomeDataTest data;
	protected ServerThreadpoolTest pool;
	
	void setup()
	{
		state = new SocketTaskStateTest();
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
	}
}

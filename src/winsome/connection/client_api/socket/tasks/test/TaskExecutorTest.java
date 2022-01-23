package winsome.connection.client_api.socket.tasks.test;

class TaskExecutorTest
{
	ConnectionHandlerTest connection;
	ApplicationAPITest app_api;

	void setup()
	{
		connection = new ConnectionHandlerTest();
		app_api = new ApplicationAPITest();
	}
}

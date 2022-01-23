package winsome.console_app.commands.test;

import java.lang.reflect.Field;

import winsome.client_app.ClientAppAPI;

class CommandExecutorTest
{
	protected TestClientAPI client_api;

	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		client_api = new TestClientAPI();
		
		//sets the api via reflex
		Field singletonAPI = ClientAppAPI.class.getDeclaredField("client_api");
		singletonAPI.setAccessible(true);
		singletonAPI.set(null, client_api);
	}
}

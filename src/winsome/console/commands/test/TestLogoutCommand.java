package winsome.console.commands.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.client.Connection;
import winsome.client.api.exceptions.NotLoggedInException;
import winsome.console.CannotExecuteException;
import winsome.console.commands.LogoutExecutor;

class TestLogoutCommand
{
	LogoutExecutor command;
	TestClientAPI client_api;

	@BeforeEach
	void setup() throws Exception
	{
		command = new LogoutExecutor(null);
		client_api = new TestClientAPI();
		
		//sets the api via reflex
		Field singletonAPI = Connection.class.getDeclaredField("client_api");
		singletonAPI.setAccessible(true);
		singletonAPI.set(null, client_api);
	}
	
	@Test
	void testConstructor() { }
	
	@Test
	void testLogout()
	{
		client_api.setLoggedIn();
		command.executeString("logout");
		client_api.checkLogoutCalled();
	}
	
	@Test
	void testLogoutNotLoggedIn()
	{
		assertThrows(NotLoggedInException.class, () -> command.executeString("logout"));
		client_api.checkLogoutCalled();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("logot"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("logou"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("logouts"));
	}
}

package winsome.console.commands.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.client.Connection;
import winsome.client.api.exceptions.*;
import winsome.console.CannotExecuteException;
import winsome.console.commands.LoginExecutor;

class TestLoginCommand
{
	LoginExecutor command;
	TestClientAPI client_api;

	@BeforeEach
	void setup() throws Exception
	{
		command = new LoginExecutor(null);
		client_api = new TestClientAPI();
		
		//sets the api via reflex
		Field singletonAPI = Connection.class.getDeclaredField("client_api");
		singletonAPI.setAccessible(true);
		singletonAPI.set(null, client_api);
	}

	@Test
	void testConstructor() { }
	
	@Test
	void testLoginCorrect()
	{
		command.executeString("login username password");
		client_api.checkLoginArguments("username", "password");
	}
	
	@Test
	void testLoginNoUsernameFails()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("login  password"));
	}
	
	@Test
	void testLoginNoPasswordFails()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("login username "));
	}
	
	@Test
	void testLoginUnknownUsernameFails()
	{
		client_api.setUnknownUsername();
		assertThrows(UnknownUsernameException.class, () -> command.executeString("login username password"));
	}
	
	@Test
	void testLoginIncorrectPasswordFails()
	{
		client_api.setIncorrectPassword();
		assertThrows(IncorrectPasswordException.class, () -> command.executeString("login username password"));
	}
	
	@Test
	void testLoginAlreadyLoggedInFails()
	{
		client_api.setLoggedIn();
		assertThrows(AlreadyLoggedInException.class, () -> command.executeString("login username password"));
	}
}

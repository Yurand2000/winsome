package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ExitExecutor;

class TestExitCommand
{
	ExitExecutor command;
	TestClientAPI client_api;

	@BeforeEach
	void setup() throws Exception
	{
		command = new ExitExecutor(null);
		client_api = new TestClientAPI();
		
		//sets the api via reflex
		Field singletonAPI = ClientAppAPI.class.getDeclaredField("client_api");
		singletonAPI.setAccessible(true);
		singletonAPI.set(null, client_api);
	}
	
	@Test
	void testExitLoggedIn()
	{
		assertFalse(Thread.currentThread().isInterrupted());
		
		client_api.setLoggedIn();
		command.executeString("exit");
		client_api.checkLogoutCalled();
		
		assertTrue(Thread.currentThread().isInterrupted());
	}

	@Test
	void testExitNotLoggedIn()
	{
		assertFalse(Thread.currentThread().isInterrupted());
		
		command.executeString("exit");
		client_api.checkLogoutCalled();
		
		assertTrue(Thread.currentThread().isInterrupted());
	}
	
	@Test
	void testExitFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("exi"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("exitt"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("exot"));
	}
}

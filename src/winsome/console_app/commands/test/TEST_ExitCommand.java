package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ExitExecutor;

class TEST_ExitCommand extends CommandExecutorTest
{
	ExitExecutor command;

	@BeforeEach
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		command = new ExitExecutor(null);
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

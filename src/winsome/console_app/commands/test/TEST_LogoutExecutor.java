package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.client_app.api.exceptions.NotLoggedInException;
import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.LogoutExecutor;

class TEST_LogoutExecutor extends CommandExecutorTest
{
	LogoutExecutor executor;

	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new LogoutExecutor(null);
	}
	
	@Test
	void testLogout()
	{
		client_api.setLoggedIn();
		executor.executeString("logout");
		client_api.checkLogoutCalled();
	}
	
	@Test
	void testLogoutNotLoggedIn()
	{
		assertThrows(NotLoggedInException.class, () -> executor.executeString("logout"));
		client_api.checkLogoutCalled();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("logot"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("logou"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("logouts"));
	}
}

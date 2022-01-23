package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ListFollowersExecutor;

class TEST_ListFollowersExecutor extends CommandExecutorTest
{
	ListFollowersExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new ListFollowersExecutor(null);
	}

	@Test
	void test()
	{
		executor.executeString("list followers");
		client_api.getLoggedAPI().checkListFollowersExecuted();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("listfollowers"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("liss followers"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("list followert"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("list followeert"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("lisst followers"));
	}
}

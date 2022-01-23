package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ListFollowingExecutor;

class TEST_ListFollowingExecutor extends CommandExecutorTest
{
	ListFollowingExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new ListFollowingExecutor(null);
	}

	@Test
	void test()
	{
		executor.executeString("list following");
		client_api.getLoggedAPI().checkListFollowingExecuted();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("listfollowing"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("liss following"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("list followin"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("list followiing"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("lisst following"));
	}
}

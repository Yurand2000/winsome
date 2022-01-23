package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ListUsersExecutor;

class TEST_ListUsersExecutor extends CommandExecutorTest
{
	ListUsersExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new ListUsersExecutor(null);
	}

	@Test
	void test()
	{
		executor.executeString("list users");
		client_api.getLoggedAPI().checkListUsersExecuted();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("listusers"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("liss users"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("list user"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("list userss"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("lisst users"));
	}
}

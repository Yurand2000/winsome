package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.UnfollowUserExecutor;

class TEST_UnfollowUserExecutor extends CommandExecutorTest
{
	UnfollowUserExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new UnfollowUserExecutor(null);
	}

	@Test
	void test()
	{
		client_api.getLoggedAPI().setUnfollowUserExpected("Nicola");
		assertDoesNotThrow(() -> executor.executeString("unfollow Nicola"));
		client_api.getLoggedAPI().checkUnfollowUserExecuted();
		
		client_api.getLoggedAPI().setUnfollowUserExpected("Lucia");
		assertDoesNotThrow(() -> executor.executeString("unfollow Lucia"));
		client_api.getLoggedAPI().checkUnfollowUserExecuted();
		
		client_api.getLoggedAPI().setUnfollowUserExpected("Amedeo");
		assertDoesNotThrow(() -> executor.executeString("unfollow Amedeo"));
		client_api.getLoggedAPI().checkUnfollowUserExecuted();
		
		client_api.getLoggedAPI().setUnfollowUserExpected("Yurand");
		assertDoesNotThrow(() -> executor.executeString("unfollow Yurand"));
		client_api.getLoggedAPI().checkUnfollowUserExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("unfollowLuigi"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("unfollowL uigi"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("unfolloow luigi"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("unfollo luca"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("unfollew nicola"));
	}

}

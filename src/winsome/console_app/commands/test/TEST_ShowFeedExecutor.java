package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ShowFeedExecutor;

class TEST_ShowFeedExecutor extends CommandExecutorTest
{
	ShowFeedExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new ShowFeedExecutor(null);
	}

	@Test
	void test()
	{
		executor.executeString("show feed");
		client_api.getLoggedAPI().checkShowFeedExecuted();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("showfeed"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("sho feed"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("show feeed"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("show fed"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("shows feed"));
	}
}

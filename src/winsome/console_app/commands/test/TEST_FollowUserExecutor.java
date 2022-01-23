package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.FollowUserExecutor;

class TEST_FollowUserExecutor extends CommandExecutorTest
{
	FollowUserExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new FollowUserExecutor(null);
	}

	@Test
	void test()
	{
		client_api.getLoggedAPI().setFollowUserExpected("Nicola");
		assertDoesNotThrow(() -> executor.executeString("follow Nicola"));
		client_api.getLoggedAPI().checkFollowUserExecuted();
		
		client_api.getLoggedAPI().setFollowUserExpected("Lucia");
		assertDoesNotThrow(() -> executor.executeString("follow Lucia"));
		client_api.getLoggedAPI().checkFollowUserExecuted();
		
		client_api.getLoggedAPI().setFollowUserExpected("Amedeo");
		assertDoesNotThrow(() -> executor.executeString("follow Amedeo"));
		client_api.getLoggedAPI().checkFollowUserExecuted();
		
		client_api.getLoggedAPI().setFollowUserExpected("Yurand");
		assertDoesNotThrow(() -> executor.executeString("follow Yurand"));
		client_api.getLoggedAPI().checkFollowUserExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("followLuigi"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("followL uigi"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("folloow luigi"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("follo luca"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("follew nicola"));
	}

}

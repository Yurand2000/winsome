package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.RewinPostExecutor;

class TEST_RewinPostExecutor extends CommandExecutorTest
{
	RewinPostExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new RewinPostExecutor(null);
	}

	@Test
	void test()
	{
		client_api.getLoggedAPI().setRewinPostExpected(500);
		assertDoesNotThrow(() -> executor.executeString("rewin 500"));
		client_api.getLoggedAPI().checkRewinPostExecuted();
		
		client_api.getLoggedAPI().setRewinPostExpected(145);
		assertDoesNotThrow(() -> executor.executeString("rewin 145"));
		client_api.getLoggedAPI().checkRewinPostExecuted();
		
		client_api.getLoggedAPI().setRewinPostExpected(0);
		assertDoesNotThrow(() -> executor.executeString("rewin 0"));
		client_api.getLoggedAPI().checkRewinPostExecuted();

		client_api.getLoggedAPI().setRewinPostExpected(10457575);
		assertDoesNotThrow(() -> executor.executeString("rewin 10457575"));
		client_api.getLoggedAPI().checkRewinPostExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rewin as5d4"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rewin445"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rewinn 45"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rewiiin 444"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rewi 15"));
	}
}

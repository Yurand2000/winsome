package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ShowPostExecutor;

class TEST_ShowPostExecutor extends CommandExecutorTest
{
	ShowPostExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new ShowPostExecutor(null);
	}

	@Test
	void test()
	{
		client_api.getLoggedAPI().setShowPostExpected(500);
		assertDoesNotThrow(() -> executor.executeString("show post 500"));
		client_api.getLoggedAPI().checkShowPostExecuted();
		
		client_api.getLoggedAPI().setShowPostExpected(145);
		assertDoesNotThrow(() -> executor.executeString("show post 145"));
		client_api.getLoggedAPI().checkShowPostExecuted();
		
		client_api.getLoggedAPI().setShowPostExpected(0);
		assertDoesNotThrow(() -> executor.executeString("show post 0"));
		client_api.getLoggedAPI().checkShowPostExecuted();

		client_api.getLoggedAPI().setShowPostExpected(10457575);
		assertDoesNotThrow(() -> executor.executeString("show post 10457575"));
		client_api.getLoggedAPI().checkShowPostExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("show post as5d4"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("show post445"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("sho post 45"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("showpost 444"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("show pot 15"));
	}
}

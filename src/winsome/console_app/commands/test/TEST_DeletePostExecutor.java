package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.DeletePostExecutor;

class TEST_DeletePostExecutor extends CommandExecutorTest
{
	DeletePostExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new DeletePostExecutor(null);
	}

	@Test
	void test()
	{
		client_api.getLoggedAPI().setDeletePostExpected(500);
		assertDoesNotThrow(() -> executor.executeString("delete 500"));
		client_api.getLoggedAPI().checkDeletePostExecuted();
		
		client_api.getLoggedAPI().setDeletePostExpected(145);
		assertDoesNotThrow(() -> executor.executeString("delete 145"));
		client_api.getLoggedAPI().checkDeletePostExecuted();
		
		client_api.getLoggedAPI().setDeletePostExpected(0);
		assertDoesNotThrow(() -> executor.executeString("delete 0"));
		client_api.getLoggedAPI().checkDeletePostExecuted();

		client_api.getLoggedAPI().setDeletePostExpected(10457575);
		assertDoesNotThrow(() -> executor.executeString("delete 10457575"));
		client_api.getLoggedAPI().checkDeletePostExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("delete as5d4"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("delete445"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("delett 45"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("deletee 444"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("delet 15"));
	}
}

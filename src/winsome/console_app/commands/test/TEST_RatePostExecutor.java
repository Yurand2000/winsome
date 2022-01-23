package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.RatePostExecutor;

class TEST_RatePostExecutor extends CommandExecutorTest
{
	RatePostExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new RatePostExecutor(null);
	}
	
	@Test
	void test()
	{
		client_api.getLoggedAPI().setRatePostExpected(50, true);
		executor.executeString("rate 50 +1");
		client_api.getLoggedAPI().checkRatePostExecuted();
		
		client_api.getLoggedAPI().setRatePostExpected(2748, false);
		executor.executeString("rate 2748 -1");
		client_api.getLoggedAPI().checkRatePostExecuted();
		
		client_api.getLoggedAPI().setRatePostExpected(15, false);
		executor.executeString("rate 15 -1");
		client_api.getLoggedAPI().checkRatePostExecuted();
		
		client_api.getLoggedAPI().setRatePostExpected(0, true);
		executor.executeString("rate 0 +1");
		client_api.getLoggedAPI().checkRatePostExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate 50+1"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate50 -1"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate 50dds -1"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rat 50 -1"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate 50 1"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate 50 5"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate 50 -4"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("rate 50 0"));
	}
}

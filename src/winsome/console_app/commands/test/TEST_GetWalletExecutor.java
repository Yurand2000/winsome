package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.GetWalletExecutor;

class TEST_GetWalletExecutor extends CommandExecutorTest
{
	GetWalletExecutor executor;

	@Override
	@BeforeEach
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new GetWalletExecutor(null);
	}

	@Test
	void test()
	{
		String wallet = executor.executeString("wallet");
		client_api.getLoggedAPI().checkGetWalletExecuted();
		assertEquals(wallet, client_api.getLoggedAPI().getWallet().toString());
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallets"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallett"));		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallee"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("walltt"));
	}

}

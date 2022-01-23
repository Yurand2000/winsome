package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.GetWalletInBitcoinExecutor;

class TEST_GetWalletInBitcoinExecutor extends CommandExecutorTest
{
	GetWalletInBitcoinExecutor executor;

	@Override
	@BeforeEach
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new GetWalletInBitcoinExecutor(null);
	}

	@Test
	void test()
	{		
		executor.executeString("wallet btc");
		client_api.getLoggedAPI().checkGetWalletInBitcoinExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("walletbtc"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallet bbc"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallet bc"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallet bttc"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallett btc"));		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("wallee btc"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("walltt btc"));
	}
}

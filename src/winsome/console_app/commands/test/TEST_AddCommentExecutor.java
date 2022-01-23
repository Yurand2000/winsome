package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.AddCommentExecutor;

class TEST_AddCommentExecutor extends CommandExecutorTest
{
	AddCommentExecutor executor;

	@Override
	@BeforeEach
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new AddCommentExecutor(null);
	}
	
	@Test
	void test()
	{
		client_api.getLoggedAPI().setAddCommentExpected(50, "comment");
		executor.executeString("comment 50 comment");
		client_api.getLoggedAPI().checkAddCommentExecuted();
		
		client_api.getLoggedAPI().setAddCommentExpected(2748, "ciao a tutti");
		executor.executeString("comment 2748 ciao a tutti");
		client_api.getLoggedAPI().checkAddCommentExecuted();
		
		client_api.getLoggedAPI().setAddCommentExpected(15, "Time to comment");
		executor.executeString("comment 15 Time to comment");
		client_api.getLoggedAPI().checkAddCommentExecuted();
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("comment50 ciao a tutti"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("comment 1421ciao 45 aa"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("commento 50 ciao a tutti"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("comnent 1421 ciao"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("commen 123 eer"));
	}
}

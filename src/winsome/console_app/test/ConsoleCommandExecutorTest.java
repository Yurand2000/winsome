package winsome.console_app.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import winsome.console_app.*;
import winsome.console_app.test.consoleCommandExecutor.*;

class ConsoleCommandExecutorTest
{	
	@Test
	void defaultExecutorWithoutSuccessorThrowsOnNullString()
	{
		ConsoleCommandExecutor testClass = new ConsoleCommandExecutor(null);
		assertThrows(CannotExecuteException.class, () -> testClass.executeString("any string"));
	}
	
	@Test
	void defaultExecutorWithSuccessorRunsOnSuccessor()
	{
		TestCommandExecutor test_child = new TestCommandExecutor();
		ConsoleCommandExecutor testClass = new ConsoleCommandExecutor(test_child);
		testClass.executeString("ciao mondo");
		test_child.checkHasExecutedAndReset();
	}
	
	@Test
	void defaultExecutorExecuteMethodRun()
	{
		TestCommandExecutorDefaultExecute test_child = new TestCommandExecutorDefaultExecute();
		ConsoleCommandExecutor testClass = new ConsoleCommandExecutor(test_child);
		assertThrows(UnsupportedOperationException.class, () -> testClass.executeString("any string"));
		test_child.checkExecuteCalled();
	}
}

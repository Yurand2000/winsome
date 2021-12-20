package winsome.console.test.consoleCommandExecutor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import winsome.console.ConsoleCommandExecutor;

public class TestCommandExecutorDefaultExecute extends ConsoleCommandExecutor
{	
	private boolean execute_called = false;
	
	public TestCommandExecutorDefaultExecute()
	{
		super(null);
	}

	@Override 
	protected boolean canExecute(String line)
	{
		return true;
	}

	@Override
	protected String execute(String line)
	{
		execute_called = true;
		return super.execute(line);
	}
	
	public void checkExecuteCalled()
	{
		assertTrue(execute_called);
	}
}

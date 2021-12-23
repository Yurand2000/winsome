package winsome.console_app.test.consoleCommandExecutor;

import static org.junit.jupiter.api.Assertions.*;

import winsome.console_app.ConsoleCommandExecutor;

public class TestCommandExecutor extends ConsoleCommandExecutor
{
	private int call_order;
	
	public TestCommandExecutor()
	{
		super(null);
		call_order = 0;
	}

	@Override 
	protected boolean canExecute(String line)
	{
		assertTrue(call_order == 0);
		call_order = 1;
		return true;
	}
	
	@Override
	protected String execute(String line)
	{
		assertTrue(call_order == 1);
		call_order = 2;
		return "TestExecutor";
	}
	
	public void checkHasExecutedAndReset()
	{
		assertTrue(call_order == 2);
		call_order = 0;
	}
}

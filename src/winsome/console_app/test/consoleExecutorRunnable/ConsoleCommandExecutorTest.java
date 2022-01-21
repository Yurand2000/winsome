package winsome.console_app.test.consoleExecutorRunnable;

import static org.junit.jupiter.api.Assertions.*;

import winsome.console_app.ConsoleCommandExecutor;

public class ConsoleCommandExecutorTest extends ConsoleCommandExecutor
{
	private String expected = null;
	private boolean executed = false;
	
	public ConsoleCommandExecutorTest()
	{
		super(null);
	}

	@Override
	protected boolean canExecute(String line)
	{
		assertEquals(expected, line);
		return true;
	}
	
	@Override
	protected String execute(String line)
	{
		Thread.currentThread().interrupt();
		executed = true;
		return "Output";
	}
	
	public void setExpectedString(String line)
	{
		expected = line;
	}
	
	public void checkExecuted()
	{
		assertTrue(executed);
		executed = false;
	}
}

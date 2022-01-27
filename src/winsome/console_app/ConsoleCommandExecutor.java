package winsome.console_app;

public class ConsoleCommandExecutor
{
	private ConsoleCommandExecutor next_executor;
	
	public ConsoleCommandExecutor(ConsoleCommandExecutor next)
	{
		next_executor = next;
	}
	
	public final String executeString(String line)
	{
		if(canExecute(line))
		{
			return execute(line);
		}
		else if(next_executor != null)
		{
			return next_executor.executeString(line);
		}
		else
		{
			throw new CannotExecuteException("Unknown command.");
		}
	}
	
	protected boolean canExecute(String line) { return false; }
	
	protected String execute(String line) { throw new UnsupportedOperationException(); }
}

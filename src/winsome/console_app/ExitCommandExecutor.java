package winsome.console_app;

public class ExitCommandExecutor extends ConsoleCommandExecutor
{
	public ExitCommandExecutor(ConsoleCommandExecutor next)
	{
		super(next);
	}

	@Override 
	protected boolean canExecute(String line) { return line.equals("exit"); }
	
	@Override
	protected String execute(String line)
	{
		Thread.currentThread().interrupt();
		return "Exiting...";
	}
}

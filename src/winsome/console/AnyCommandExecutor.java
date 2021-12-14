package winsome.console;

public class AnyCommandExecutor extends ConsoleCommandExecutor
{
	public AnyCommandExecutor(ConsoleCommandExecutor next)
	{
		super(next);
	}

	@Override 
	protected boolean canExecute(String line) { return true; }
	
	@Override
	protected String execute(String line) { return "Any Command Executor"; }
}

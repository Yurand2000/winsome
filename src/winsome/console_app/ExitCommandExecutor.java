package winsome.console_app;

import winsome.client_app.ClientAppAPI;

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
		ClientAppAPI.getAPI().logout();
		Thread.currentThread().interrupt();
		return "Exiting...";
	}
}

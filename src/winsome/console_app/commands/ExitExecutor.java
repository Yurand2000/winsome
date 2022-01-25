package winsome.console_app.commands;

import winsome.client_app.ClientAppAPI;
import winsome.client_app.api.exceptions.NotLoggedInException;
import winsome.console_app.ConsoleCommandExecutor;

public class ExitExecutor extends ConsoleCommandExecutor
{
	public ExitExecutor(ConsoleCommandExecutor next)
	{
		super(next);
	}

	@Override 
	protected boolean canExecute(String line) { return line.equals("exit"); }
	
	@Override
	protected String execute(String line)
	{		
		try
		{
			ClientAppAPI.getAPI().logout();
		}
		catch(NotLoggedInException e) { }
		finally
		{
			Thread.currentThread().interrupt();	
		}
		
		return "Exiting...";
	}
}

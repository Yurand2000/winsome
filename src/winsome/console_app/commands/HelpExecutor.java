package winsome.console_app.commands;

import winsome.console_app.ConsoleCommandExecutor;

public class HelpExecutor extends ConsoleCommandExecutor
{
	public HelpExecutor(ConsoleCommandExecutor next)
	{
		super(next);
	}

	@Override 
	protected boolean canExecute(String line) { return line.equals("help"); }
	
	@Override
	protected String execute(String line)
	{
		return	"Winsome Console Client - Help:\n" +
				"Available commands:\n"+
				"help\n"+
				"register <username> <password> <tags>\n"+
				"login <username> <password>\n"+
				"logout\n"+
				"exit\n"+
				"list users\n"+
				"list followers\n"+
				"list following\n"+
				"follow <username>\n"+
				"unfollow <username>\n"+
				"post <<title>> <<content>>\n"+
				"show feed\n"+
				"show post <id>\n"+
				"delete <id>\n"+
				"rewin <id>\n"+
				"rate <id> <+1|-1>\n"+
				"comment <id> <comment>\n"+
				"wallet\n"+
				"wallet btc\n";
	}
}

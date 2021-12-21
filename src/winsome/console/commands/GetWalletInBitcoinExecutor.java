package winsome.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import winsome.client.Connection;
import winsome.console.ConsoleCommandExecutor;

public class GetWalletInBitcoinExecutor extends ConsoleCommandExecutor
{
	//Regular expression: wallet btc
	private static final String regex_string = "^(?:wallet btc)$";
	private final Pattern regex;
	
	public GetWalletInBitcoinExecutor(ConsoleCommandExecutor next)
	{
		super(next);
		regex = Pattern.compile(regex_string);
	}

	@Override 
	protected boolean canExecute(String line)
	{
		Matcher matcher = regex.matcher(line);
		return matcher.matches();
	}
	
	@Override
	protected String execute(String line)
	{		
		Integer bitcoin = Connection.getLoggedAPI().getWalletInBitcoin();
		return "Current wallet in bitcoin: " + bitcoin.toString();
	}
}


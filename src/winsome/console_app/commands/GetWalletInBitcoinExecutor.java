package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.ConsoleCommandExecutor;

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
		Integer bitcoin = ClientAppAPI.getLoggedClientAPI().getWalletInBitcoin();
		return "Current wallet in bitcoin: " + bitcoin.toString();
	}
}


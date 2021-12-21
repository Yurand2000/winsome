package winsome.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import winsome.client.Connection;
import winsome.client.api.Wallet;
import winsome.console.ConsoleCommandExecutor;

public class GetWalletExecutor extends ConsoleCommandExecutor
{
	//Regular expression: wallet
	private static final String regex_string = "^(?:wallet)$";
	private final Pattern regex;
	
	public GetWalletExecutor(ConsoleCommandExecutor next)
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
		Wallet wallet = Connection.getLoggedAPI().getWallet();
		return wallet.toString();
	}
}

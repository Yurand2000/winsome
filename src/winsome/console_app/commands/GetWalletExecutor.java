package winsome.console_app.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import winsome.client_app.ClientAppAPI;
import winsome.client_app.api.Wallet;
import winsome.console_app.ConsoleCommandExecutor;

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
		Wallet wallet = ClientAppAPI.getLoggedClientAPI().getWallet();
		return wallet.toString();
	}
}

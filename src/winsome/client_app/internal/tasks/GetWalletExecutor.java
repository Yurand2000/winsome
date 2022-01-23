package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.client_app.api.Wallet;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.GetWalletRequest;
import winsome.connection.socket_messages.server.GetWalletAnswer;

public class GetWalletExecutor extends DefaultTaskExecutor
{
	private Wallet requested_wallet;
	
	public GetWalletExecutor()
	{
		requested_wallet = null;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		GetWalletRequest request = new GetWalletRequest();
		connection.sendMessage(request);
		
		GetWalletAnswer answer = connection.readMessage(GetWalletAnswer.class);
		requested_wallet = answer.wallet;
	}
	
	public Wallet getRequestedWallet()
	{
		return requested_wallet;
	}
}

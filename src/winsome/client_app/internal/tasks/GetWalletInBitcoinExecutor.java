package winsome.client_app.internal.tasks;

import java.io.IOException;

import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.GetWalletInBitcoinRequest;
import winsome.connection.socket_messages.server.GetWalletInBitcoinAnswer;

public class GetWalletInBitcoinExecutor extends DefaultTaskExecutor
{
	private Integer requested_total;
	
	public GetWalletInBitcoinExecutor()
	{
		requested_total = null;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		GetWalletInBitcoinRequest request = new GetWalletInBitcoinRequest();
		connection.sendMessage(request);
		
		GetWalletInBitcoinAnswer answer = connection.readMessage(GetWalletInBitcoinAnswer.class);
		requested_total = answer.total;
	}
	
	public Integer getRequestedWalletInBitcoin()
	{
		return requested_total;
	}
}

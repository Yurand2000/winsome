package winsome.server_app.internal.tasks.impl;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import winsome.client_app.api.Wallet;
import winsome.connection.socket_messages.client.GetWalletRequest;
import winsome.connection.socket_messages.server.GetWalletAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public class GetWalletTask extends LoggedUserTask
{
	@SuppressWarnings("unused")
	private final GetWalletRequest message;

	public GetWalletTask(SocketTaskState socket, WinsomeData data, GetWalletRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		GetWalletAnswer answer = makeWalletAnswer(user.wallet);
		socket.sendAnswerMessage(answer);
	}
	
	private GetWalletAnswer makeWalletAnswer(winsome.server_app.wallet.Wallet wallet)
	{
		Double total = wallet.getCurrentTotal().doubleValue();
		List<Wallet.Transaction> transactions = new ArrayList<Wallet.Transaction>();
		
		for(winsome.server_app.wallet.Wallet.Transaction transaction : wallet.getTransactions())
		{
			transactions.add(
				new Wallet.Transaction(
					Date.from(transaction.timestamp.atOffset(ZoneOffset.UTC).toInstant()),
					transaction.amount.doubleValue()
			));
		}
		
		return new GetWalletAnswer(total, transactions);
	}
}

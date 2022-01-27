package winsome.server_app.internal.tasks.impl;

import winsome.connection.random.RandomGenerator;
import winsome.connection.random.RandomGeneratorImpl;
import winsome.connection.socket_messages.client.GetWalletInBitcoinRequest;
import winsome.connection.socket_messages.server.GetWalletInBitcoinAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;
import winsome.server_app.wallet.Wallet;

public class GetWalletInBitcoinTask extends LoggedUserTask
{
	@SuppressWarnings("unused")
	private final GetWalletInBitcoinRequest message;

	public GetWalletInBitcoinTask(SocketTaskState socket, WinsomeData data, GetWalletInBitcoinRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		GetWalletInBitcoinAnswer answer = makeWalletAnswer(user.getWallet());
		socket.sendAnswerMessage(answer);
	}
	
	private GetWalletInBitcoinAnswer makeWalletAnswer(Wallet wallet)
	{
		RandomGenerator random = new RandomGeneratorImpl();
		Double current_amount = wallet.getCurrentTotal().doubleValue() * random.next();
		
		return new GetWalletInBitcoinAnswer(current_amount.intValue());
	}
}

package winsome.connection.socket_messages.server;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.client_app.api.Wallet;
import winsome.connection.socket_messages.Message;

@JsonTypeName("get_wallet_answer")
public class GetWalletAnswer extends Message
{
	public final Wallet wallet;
	
	@SuppressWarnings("unused")
	private GetWalletAnswer() { wallet = null; }

	public GetWalletAnswer(Double total, List<Wallet.Transaction> transactions)
	{
		this.wallet = new Wallet(total, transactions);
	}
	
	public GetWalletAnswer(Wallet wallet)
	{
		this.wallet = wallet;
	}
}

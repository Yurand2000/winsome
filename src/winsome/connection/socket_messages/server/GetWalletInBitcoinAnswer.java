package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("get_wallet_btc_answer")
public class GetWalletInBitcoinAnswer extends Message
{
	public final Integer total;
	
	@SuppressWarnings("unused")
	private GetWalletInBitcoinAnswer() { total = null; }
	
	public GetWalletInBitcoinAnswer(Integer total)
	{
		this.total = total;
	}
}

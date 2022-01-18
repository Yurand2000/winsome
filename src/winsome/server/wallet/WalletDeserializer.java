package winsome.server.wallet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import winsome.generic.SerializerWrapper;
import winsome.server.wallet.Wallet.Transaction;

public class WalletDeserializer extends StdDeserializer<Wallet>
{
	private static final long serialVersionUID = 1L;

	private WalletDeserializer()
	{
		super(Wallet.class);
	}

	@Override
	public Wallet deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
	{
		TreeNode tn = p.readValueAsTree();
		
		TreeNode total_token = tn.get("current_total");
		TreeNode transactions_token = tn.get("transactions");
		if(total_token != null && total_token.isValueNode() && transactions_token != null && transactions_token.isArray())
		{
			Long total = Long.parseLong(total_token.toString());
			List<Transaction> transactions = new LinkedList<Transaction>();
			for(int i = 0; i < transactions_token.size(); i++)
			{
				transactions.add( SerializerWrapper.deserialize(transactions_token.get(i), Transaction.class) );
			}
			return new Wallet(total, transactions);
		}
		else
		{
			throw new IOException();
		}
	}
}

package winsome.client_app.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.Date;

/**
 * The wallet of a user. It displays the current total amount of wincoins and the list of transactions happened in time.
 */
public class Wallet
{
	@JsonProperty public final Double current_total;
	@JsonProperty public final List<Transaction> transactions;
	
	@SuppressWarnings("unused")
	private Wallet() { current_total = null; transactions = null; }
	
	public Wallet(Double current_total, List<Transaction> transactions)
	{
		this.current_total = current_total;
		List<Transaction> copy_transactions = new ArrayList<Transaction>(transactions.size());
		for(Transaction t : transactions)
		{
			copy_transactions.add(new Transaction(t));
		}
		this.transactions = unmodifiableList(copy_transactions);
	}
	
	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		string.append("Wallet:\n");
		string.append("  Current Total: ");
		string.append(current_total);
		string.append("\n  Transactions:\n");
		for(int i = 0; i < transactions.size(); i++)
		{
			string.append("  ");
			string.append(transactions.get(i).toString());
			string.append('\n');
		}
		return string.toString();
	}
	
	@Override
	public boolean equals(Object w)
	{
		if(w.getClass() == Wallet.class)
		{
			Wallet cast = (Wallet) w;
			
			return current_total == cast.current_total &&
				transactions.equals(cast.transactions);
		}
		else
		{
			return false;
		}
	}
	
	public static class Transaction
	{
		@JsonProperty public final Date timestamp;
		@JsonProperty public final Double amount;
		
		@SuppressWarnings("unused")
		private Transaction() { timestamp = null; amount = null; }
		
		public Transaction(Date timestamp, Double amount)
		{
			this.timestamp = timestamp;
			this.amount = amount;
		}
		
		public Transaction(Transaction transaction)
		{
			this(transaction.timestamp, transaction.amount);
		}
		
		@Override
		public String toString()
		{
			return "Date: " + timestamp.toString() + ". Amount: " + amount.toString();
		}
		
		@Override
		public boolean equals(Object w)
		{
			if(w.getClass() == Transaction.class)
			{
				Transaction cast = (Transaction) w;
				
				return timestamp == cast.timestamp &&
					amount == cast.amount;
			}
			else
			{
				return false;
			}
		}
	}
}

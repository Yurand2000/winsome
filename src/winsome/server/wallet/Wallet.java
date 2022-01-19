package winsome.server.wallet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Wallet implements Cloneable
{
	@JsonProperty() private Long current_total;
	@JsonProperty() private List<Transaction> transactions;
	
	public Wallet()
	{
		current_total = (long) 0;
		transactions = new ArrayList<Transaction>();
	}
	
	public Wallet(Long total, List<Transaction> transactions)
	{
		this.current_total = total;
		this.transactions = new ArrayList<Transaction>(transactions);
	}
	
	@Override
	public synchronized Wallet clone()
	{
		Wallet wallet = new Wallet(current_total, transactions);
		return wallet;
	}
	
	@JsonIgnore
	public synchronized Long getCurrentTotal()
	{
		return current_total;
	}
	
	@JsonIgnore
	public synchronized List<Transaction> getTransactions()
	{
		return new ArrayList<Transaction>(transactions);
	}
	
	public synchronized void addTransaction(Long amount)
	{
		Transaction new_transaction = new Transaction(LocalDateTime.now(), amount);
		transactions.add(new_transaction);
		current_total = current_total + amount;
	}
	
	public static class Transaction
	{
		@JsonProperty() public final LocalDateTime timestamp;
		@JsonProperty() public final Long amount;
		
		@SuppressWarnings("unused")
		private Transaction() { timestamp = LocalDateTime.MIN; amount = 0L; }
		
		public Transaction(LocalDateTime timestamp, Long amount)
		{
			this.timestamp = timestamp;
			this.amount = amount;
		}
	}
}

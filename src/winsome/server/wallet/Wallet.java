package winsome.server.wallet;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//@JsonDeserialize(using= WalletDeserializer.class)
public class Wallet
{
	@JsonProperty()
	private Long current_total;
	
	@JsonProperty()
	private List<Transaction> transactions;
	
	public Wallet()
	{
		current_total = (long) 0;
		transactions = new LinkedList<Transaction>();
	}
	
	public Wallet(Long total, List<Transaction> transactions)
	{
		this.current_total = total;
		this.transactions = new LinkedList<Transaction>(transactions);
	}
	
	@JsonIgnore
	public synchronized Long getCurrentTotal()
	{
		return current_total;
	}
	
	@JsonIgnore
	public synchronized List<Transaction> getTransactions()
	{
		return Collections.unmodifiableList(transactions);
	}
	
	public synchronized void addTransaction(Long amount)
	{
		Transaction new_transaction = new Transaction(LocalDateTime.now(), amount);
		transactions.add(new_transaction);
		current_total = current_total + amount;
	}
	
	public static class Transaction
	{
		public final LocalDateTime timestamp;
		public final Long amount;
		
		@SuppressWarnings("unused")
		private Transaction() { timestamp = LocalDateTime.MIN; amount = 0L; }
		
		public Transaction(LocalDateTime timestamp, Long amount)
		{
			this.timestamp = timestamp;
			this.amount = amount;
		}
	}
}

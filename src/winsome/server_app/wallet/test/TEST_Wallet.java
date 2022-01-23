package winsome.server_app.wallet.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.wallet.Wallet;
import winsome.server_app.wallet.Wallet.Transaction;

class TEST_Wallet
{

	@Test
	void constructors()
	{
		Wallet default_constructed = new Wallet();
		assertEquals(default_constructed.getCurrentTotal(), 0);
		assertTrue(default_constructed.getTransactions().isEmpty());
		
		List<Transaction> transactions = Arrays.asList(
			new Transaction(LocalDateTime.now(), (long) 30),
			new Transaction(LocalDateTime.now(), (long) 20));
		Wallet param_constructed = new Wallet((long) 50, transactions);
		assertEquals(param_constructed.getCurrentTotal(), 50);
		
		transactions = param_constructed.getTransactions();
		assertEquals(transactions.size(), 2);
		assertEquals(transactions.get(0).amount, 30);
		assertEquals(transactions.get(1).amount, 20);
	}

	@Test
	void checkAddTransaction()
	{
		Wallet wallet = new Wallet();
		assertTrue(wallet.getTransactions().isEmpty());
		
		wallet.addTransaction((long) 20);
		assertEquals(wallet.getCurrentTotal(), 20);
		assertEquals(wallet.getTransactions().get(0).amount, 20);
		
		wallet.addTransaction((long) 50);
		assertEquals(wallet.getCurrentTotal(), 70);
		assertEquals(wallet.getTransactions().get(0).amount, 20);
		assertEquals(wallet.getTransactions().get(1).amount, 50);
	}
	
	@Test
	void checkClone()
	{
		Wallet wallet = new Wallet();
		assertTrue(wallet.getTransactions().isEmpty());
		
		wallet.addTransaction((long) 20);
		assertEquals(wallet.getCurrentTotal(), 20);
		assertEquals(wallet.getTransactions().get(0).amount, 20);
		
		Wallet clone = wallet.clone();
		assertEquals(clone.getCurrentTotal(), 20);
		assertEquals(clone.getTransactions().get(0).amount, 20);
		
		wallet.addTransaction((long) 50);
		assertEquals(clone.getCurrentTotal(), 20);
		assertEquals(clone.getTransactions().get(0).amount, 20);
	}
	
	@Test
	@SuppressWarnings("unused")
	void testSerialization() throws IOException
	{
		Wallet wallet = new Wallet();
		wallet.addTransaction((long) 20);
		wallet.addTransaction((long) 50);
		
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(wallet); } );
		byte[] data = SerializerWrapper.serialize(wallet);
		
		assertDoesNotThrow(() -> { Wallet w = SerializerWrapper.deserialize(data, Wallet.class); } );
		Wallet w = SerializerWrapper.deserialize(data, Wallet.class);
		
		assertEquals(wallet.getCurrentTotal(), w.getCurrentTotal());
		assertEquals(wallet.getTransactions().get(0).amount, w.getTransactions().get(0).amount);
		assertEquals(wallet.getTransactions().get(1).amount, w.getTransactions().get(1).amount);
	}
}

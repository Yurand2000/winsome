package winsome.connection.client_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.Wallet;
import winsome.connection.client_api.socket.tasks.GetWalletExecutor;
import winsome.connection.socket_messages.client.GetWalletRequest;
import winsome.connection.socket_messages.server.GetWalletAnswer;

class TEST_GetWalletExecutor extends TaskExecutorTest
{
	private GetWalletExecutor task;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		task = new GetWalletExecutor();
	}

	@Test
	void test()
	{
		Wallet wallet = makeWallet();
		GetWalletAnswer answer = new GetWalletAnswer(wallet);
		connection.setReceiveMessage(answer);
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == GetWalletRequest.class);
		assertEquals(task.getRequestedWallet(), wallet);
	}

	private Wallet makeWallet()
	{
		return new Wallet(0.0, Arrays.asList(new Wallet.Transaction[0]));
	}
}

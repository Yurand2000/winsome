package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.internal.tasks.GetWalletInBitcoinExecutor;
import winsome.connection.socket_messages.client.GetWalletInBitcoinRequest;
import winsome.connection.socket_messages.server.GetWalletInBitcoinAnswer;

class TEST_GetWalletInBitcoinExecutor extends TaskExecutorTest
{
	private GetWalletInBitcoinExecutor task;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		task = new GetWalletInBitcoinExecutor();
	}

	@Test
	void test()
	{
		Integer current_total = 48641;
		GetWalletInBitcoinAnswer answer = new GetWalletInBitcoinAnswer(current_total);
		connection.setReceiveMessage(answer);
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == GetWalletInBitcoinRequest.class);
		assertEquals(task.getRequestedWalletInBitcoin(), current_total);
	}
}

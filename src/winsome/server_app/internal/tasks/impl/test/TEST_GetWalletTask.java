package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.GetWalletRequest;
import winsome.connection.socket_messages.server.GetWalletAnswer;
import winsome.server_app.internal.tasks.impl.GetWalletTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

class TEST_GetWalletTask extends SocketTaskTest
{
	GetWalletTask task;
	GetWalletRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new GetWalletRequest();
		task = new GetWalletTask(state, data, message);
		
		User user = UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") });
		user.getWallet().addTransaction(50L);
		user.getWallet().addTransaction(70L);
		data.getUsers().put( "user", user );
		state.set_user_called = true;
	}

	@Test
	void testNormalOperation()
	{		
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == GetWalletAnswer.class);
		GetWalletAnswer answer = (GetWalletAnswer) state.sent_message;
		assertEquals(answer.wallet.current_total, 120);
		assertEquals(answer.wallet.transactions.size(), 2);
		assertEquals(answer.wallet.transactions.get(0).amount, 50);
		assertEquals(answer.wallet.transactions.get(1).amount, 70);
	}

}

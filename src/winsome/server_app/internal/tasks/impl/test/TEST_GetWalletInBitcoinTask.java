package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.GetWalletInBitcoinRequest;
import winsome.connection.socket_messages.server.GetWalletInBitcoinAnswer;
import winsome.server_app.internal.tasks.impl.GetWalletInBitcoinTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

class TEST_GetWalletInBitcoinTask extends SocketTaskTest
{
	GetWalletInBitcoinTask task;
	GetWalletInBitcoinRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new GetWalletInBitcoinRequest();
		task = new GetWalletInBitcoinTask(state, data, message);
		
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
		
		assertTrue(state.sent_message.getClass() == GetWalletInBitcoinAnswer.class);
		GetWalletInBitcoinAnswer answer = (GetWalletInBitcoinAnswer) state.sent_message;
		assertTrue(answer.total >= (120.0 * 35.0)); //the random change rate varies between 35 and 50 bitcoins to wincoins.
		assertTrue(answer.total <= (120.0 * 50.0));
	}

}

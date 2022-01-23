package winsome.connection.client_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.APIException;
import winsome.client_app.api.PostShort;
import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.client_api.socket.tasks.LoginTaskExecutor;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.client.LoginRequest;
import winsome.connection.socket_messages.server.LoginAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;

class TEST_LoginTaskExecutor extends TaskExecutorTest
{	
	private LoginTaskExecutor task;
	private Runnable wallet_notification_runnable;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		wallet_notification_runnable = () -> { };
		task = new LoginTaskExecutor("user", "pass", wallet_notification_runnable);
	}

	@Test
	void testNormalOperation()
	{
		LoginAnswer answer = new LoginAnswer(
			new String[] {"Luca"},
			new String[] {"Gianni"},
			new LoginAnswer.PostIdAndTitle[] { new LoginAnswer.PostIdAndTitle(50, "Titolo") },
			"multicast_address"
		);
		connection.setReceiveMessage(answer);
		app_api.getWalletNotifier().setExpectedArguments("multicast_address", wallet_notification_runnable);
		
		task.run(connection, app_api);
		
		assertTrue(connection.connect_called);
		assertFalse(connection.disconnect_called);
		assertTrue(connection.sent_message.getClass() == LoginRequest.class);
		LoginRequest request = (LoginRequest) connection.sent_message;
		assertEquals(request.username, "user");
		assertEquals(request.password, "pass");
		
		assertTrue(app_api.getFollowers().contains("Luca"));
		assertTrue(app_api.getFollowing().contains("Gianni"));
		assertTrue(app_api.getBlog().containsKey(50));
		PostShort post = app_api.getBlog().get(50);
		assertEquals(post.postId, 50);
		assertEquals(post.author, "user");
		assertEquals(post.title, "Titolo");
		
		app_api.getWalletNotifier().checkRegisterCalled();
		app_api.getFollowerUpdater().checkRegisterCalled();
	}

	@Test
	void testOnException()
	{
		connection.setReceiveMessage(new RequestExceptionAnswer("exception"));
		
		assertThrows(APIException.class, () -> task.run(connection, app_api));

		assertTrue(connection.connect_called);
		assertTrue(connection.disconnect_called);
	}

	@Test
	void testOnException2()
	{
		connection.setReceiveMessage(new Message());
		
		assertThrows(ServerInternalException.class, () -> task.run(connection, app_api));

		assertTrue(connection.connect_called);
		assertTrue(connection.disconnect_called);
	}
}

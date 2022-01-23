package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.User;
import winsome.client_app.internal.tasks.ListUserExecutor;
import winsome.connection.socket_messages.client.ListUserRequest;
import winsome.connection.socket_messages.server.ListUserAnswer;

class TEST_ListUserExecutor extends TaskExecutorTest
{
	private ListUserExecutor task;
	private List<User> list;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		list = new ArrayList<User>();
		task = new ListUserExecutor(list);
	}

	@Test
	void test()
	{
		ListUserAnswer answer = new ListUserAnswer(new String[]{ "Utente1", "Utente2" });
		connection.setReceiveMessage(answer);
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == ListUserRequest.class);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).username, "Utente1");
		assertEquals(list.get(1).username, "Utente2");
	}

}

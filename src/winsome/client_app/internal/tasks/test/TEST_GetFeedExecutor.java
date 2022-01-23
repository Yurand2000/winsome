package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.PostShort;
import winsome.client_app.internal.tasks.GetFeedExecutor;
import winsome.connection.socket_messages.client.GetFeedRequest;
import winsome.connection.socket_messages.server.GetFeedAnswer;

class TEST_GetFeedExecutor extends TaskExecutorTest
{
	private GetFeedExecutor task;
	private List<PostShort> list;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		list = new ArrayList<PostShort>();
		task = new GetFeedExecutor(list);
	}

	@Test
	void test()
	{
		GetFeedAnswer answer = new GetFeedAnswer(new PostShort[] {
			new PostShort(1, "Luigi", "Post1"),
			new PostShort(6, "Luigi", "Post2")
		});
		connection.setReceiveMessage(answer);
		assertTrue(list.isEmpty());
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == GetFeedRequest.class);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).postId, 1);
		assertEquals(list.get(0).author, "Luigi");
		assertEquals(list.get(0).title, "Post1");
		assertEquals(list.get(1).postId, 6);
		assertEquals(list.get(1).author, "Luigi");
		assertEquals(list.get(1).title, "Post2");
	}

}

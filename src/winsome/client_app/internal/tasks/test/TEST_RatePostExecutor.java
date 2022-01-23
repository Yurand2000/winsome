package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.PostOwnedException;
import winsome.client_app.internal.tasks.RatePostExecutor;
import winsome.connection.socket_messages.client.RatePostRequest;
import winsome.connection.socket_messages.server.RatePostAnswer;

class TEST_RatePostExecutor extends TaskExecutorTest
{
	private RatePostExecutor task;
	private Integer postId;
	private Boolean liked;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		postId = 50;
		liked = true;
		task = new RatePostExecutor(postId, liked);
	}

	@Test
	void testNormalExecution()
	{
		RatePostAnswer answer = new RatePostAnswer();
		connection.setReceiveMessage(answer);
		assertFalse(app_api.getBlog().containsKey(postId));
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == RatePostRequest.class);
		assertEquals( ((RatePostRequest)connection.sent_message).postId, postId );
		assertEquals( ((RatePostRequest)connection.sent_message).liked, liked );
	}
	
	@Test
	void testIsPostAuthor()
	{
		app_api.getBlog().put(postId, null);

		assertThrows(PostOwnedException.class, () -> task.run(connection, app_api));
		
		assertEquals(connection.sent_message, null);
	}
}

package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.PostNotOwnedException;
import winsome.client_app.internal.tasks.DeletePostExecutor;
import winsome.connection.socket_messages.client.DeletePostRequest;
import winsome.connection.socket_messages.server.DeletePostAnswer;

class TEST_DeletePostExecutor extends TaskExecutorTest
{
	private DeletePostExecutor task;
	private Integer postId;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		postId = 50;
		task = new DeletePostExecutor(postId);
	}

	@Test
	void testNormalExecution()
	{
		DeletePostAnswer answer = new DeletePostAnswer();
		connection.setReceiveMessage(answer);
		app_api.getBlog().put(postId, null);
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == DeletePostRequest.class);
		assertEquals( ((DeletePostRequest)connection.sent_message).postId, postId );
		assertFalse(app_api.getBlog().containsKey(postId));
	}

	@Test
	void testPostNotOwned()
	{
		assertFalse(app_api.getBlog().containsKey(postId));
		
		assertThrows(PostNotOwnedException.class, () -> task.run(connection, app_api));
		
		assertEquals(connection.sent_message, null);
	}
}

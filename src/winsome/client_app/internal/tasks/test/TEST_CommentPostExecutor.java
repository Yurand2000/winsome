package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.PostOwnedException;
import winsome.client_app.internal.tasks.CommentPostExecutor;
import winsome.connection.socket_messages.client.CommentPostRequest;
import winsome.connection.socket_messages.server.CommentPostAnswer;

class TEST_CommentPostExecutor extends TaskExecutorTest
{
	private CommentPostExecutor task;
	private Integer postId;
	private String comment;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		postId = 50;
		comment = "Comment";
		task = new CommentPostExecutor(postId, comment);
	}

	@Test
	void testNormalExecution()
	{
		CommentPostAnswer answer = new CommentPostAnswer();
		connection.setReceiveMessage(answer);
		assertFalse(app_api.getBlog().containsKey(postId));
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == CommentPostRequest.class);
		assertEquals( ((CommentPostRequest)connection.sent_message).postId, postId );
		assertEquals( ((CommentPostRequest)connection.sent_message).comment, comment );
	}
	
	@Test
	void testIsPostAuthor()
	{
		app_api.getBlog().put(postId, null);

		assertThrows(PostOwnedException.class, () -> task.run(connection, app_api));
		
		assertEquals(connection.sent_message, null);
	}
}

package winsome.connection.client_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.client_api.socket.tasks.RewinPostExecutor;
import winsome.connection.socket_messages.client.RewinPostRequest;
import winsome.connection.socket_messages.server.RewinPostAnswer;

class TEST_RewinPostExecutor extends TaskExecutorTest
{
	private RewinPostExecutor task;
	private Integer postId;
	private Integer newpostId;
	private String title;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		postId = 50;
		newpostId = 51;
		task = new RewinPostExecutor(postId);
	}

	@Test
	void testNormalExecution()
	{
		RewinPostAnswer answer = new RewinPostAnswer(newpostId, title);
		connection.setReceiveMessage(answer);
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == RewinPostRequest.class);
		assertEquals( ((RewinPostRequest)connection.sent_message).postId, postId );
		assertTrue(app_api.getBlog().containsKey(newpostId));
		assertEquals( task.getNewPostId(), newpostId );
	}
}

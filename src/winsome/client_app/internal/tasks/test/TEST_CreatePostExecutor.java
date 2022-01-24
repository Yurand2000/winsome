package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.internal.tasks.CreatePostExecutor;
import winsome.connection.socket_messages.client.CreatePostRequest;
import winsome.connection.socket_messages.server.CreatePostAnswer;

class TEST_CreatePostExecutor extends TaskExecutorTest
{
	private CreatePostExecutor task;
	private String title;
	private String content;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		title = "title";
		content = "content";
		task = new CreatePostExecutor(title, content);
	}

	@Test
	void test()
	{
		Integer newPostId = 50;
		CreatePostAnswer answer = new CreatePostAnswer(newPostId);
		connection.setReceiveMessage(answer);
		
		task.run(connection, app_api);
		
		assertTrue( connection.sent_message.getClass() == CreatePostRequest.class );
		assertEquals( ((CreatePostRequest)connection.sent_message).title, title);
		assertEquals( ((CreatePostRequest)connection.sent_message).content, content);
		assertEquals( task.getNewPostId(), newPostId );
	}

}

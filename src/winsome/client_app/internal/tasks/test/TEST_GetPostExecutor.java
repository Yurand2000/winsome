package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.Post;
import winsome.client_app.internal.tasks.GetPostExecutor;
import winsome.connection.socket_messages.client.GetPostRequest;
import winsome.connection.socket_messages.server.GetPostAnswer;

class TEST_GetPostExecutor extends TaskExecutorTest
{
	private GetPostExecutor task;
	private Integer postId;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		postId = 50;
		task = new GetPostExecutor(postId);
	}

	@Test
	void test()
	{
		Post post = makePost();
		GetPostAnswer answer = new GetPostAnswer(post);
		connection.setReceiveMessage(answer);
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == GetPostRequest.class);
		assertEquals( ((GetPostRequest)connection.sent_message).postId, postId );
		
		assertEquals(post, task.getRetrivedPost());
	}

	private Post makePost()
	{
		return new Post(postId, "Author", "Title", "Content", 0, 0, new ArrayList<Post.Comment>());
	}
}

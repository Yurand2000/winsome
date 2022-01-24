package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.CreatePostRequest;
import winsome.connection.socket_messages.server.CreatePostAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.CreatePostTask;
import winsome.server_app.post.ContentPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_CreatePostTask extends SocketTaskTest
{
	CreatePostTask task;
	CreatePostRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new CreatePostRequest("title", "content");
		task = new CreatePostTask(state, data, message);
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		state.set_user_called = true;
	}
	
	@Test
	void testNormalOperation()
	{
		assertEquals(data.getUsers().get("user").getPosts().size(), 0);
		assertEquals(data.getPosts().size(), 0);
		
		task.run(pool);
		
		assertTrue(data.getPostFactory().makeNewPost_called);
		assertEquals(data.getUsers().get("user").getPosts().size(), 1);
		assertEquals(data.getPosts().size(), 1);
		Integer postId = data.getUsers().get("user").getPosts().get(0);
		ContentPost post = (ContentPost) data.getPosts().get(postId);
		assertTrue(post != null);		
		assertEquals(post.content.title, "title");
		assertEquals(post.content.author, "user");
		assertEquals(post.content.content, "content");
		
		assertTrue(state.sent_message.getClass() == CreatePostAnswer.class);
		CreatePostAnswer answer = (CreatePostAnswer) state.sent_message;
		assertEquals(answer.newPostId, postId);
	}
	
	@Test
	void testTitleTooLong()
	{
		assertEquals(data.getUsers().get("user").getPosts().size(), 0);
		assertEquals(data.getPosts().size(), 0);
		
		message = new CreatePostRequest("title too long for comfort", "content");
		task = new CreatePostTask(state, data, message);
		
		task.run(pool);

		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertEquals(data.getUsers().get("user").getPosts().size(), 0);
		assertEquals(data.getPosts().size(), 0);
	}
	
	@Test
	void testContentTooLong()
	{
		assertEquals(data.getUsers().get("user").getPosts().size(), 0);
		assertEquals(data.getPosts().size(), 0);
		
		StringBuilder content = new StringBuilder();
		for(int i = 0; i < 501; i++)
			content.append('a');
		
		message = new CreatePostRequest("title", content.toString());
		task = new CreatePostTask(state, data, message);
		
		task.run(pool);

		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
		assertEquals(data.getUsers().get("user").getPosts().size(), 0);
		assertEquals(data.getPosts().size(), 0);
	}
}

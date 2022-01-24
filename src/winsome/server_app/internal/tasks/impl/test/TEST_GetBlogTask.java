package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.GetBlogRequest;
import winsome.connection.socket_messages.server.GetBlogAnswer;
import winsome.server_app.internal.tasks.impl.GetBlogTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_GetBlogTask extends SocketTaskTest
{
	GetBlogTask task;
	GetBlogRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new GetBlogRequest();
		task = new GetBlogTask(state, data, message);
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "Laura", UserFactory.makeNewUser("Laura", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "Nicola", UserFactory.makeNewUser("Nicola", "pass", new Tag[]{ new Tag("tag1") }) );
		
		GenericPost post1 = data.getPostFactory().makeNewPostId(1, "titolo1", "user", "content1");
		GenericPost post2 = data.getPostFactory().makeRewinPostId(2, post1.postId, "user");
		GenericPost post3 = data.getPostFactory().makeNewPostId(3, "titolo3", "Nicola", "content3");
		data.getPosts().put(post1.postId, post1);
		data.getPosts().put(post2.postId, post2);
		data.getPosts().put(post3.postId, post3);
		data.getUsers().get("user").addPost(post1.postId);
		data.getUsers().get("user").addPost(post2.postId);
		data.getUsers().get("Nicola").addPost(post3.postId);
		state.set_user_called = true;
	}
	
	@Test
	void testNormalOperation()
	{
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == GetBlogAnswer.class);
		GetBlogAnswer answer = (GetBlogAnswer) state.sent_message;
		assertEquals(answer.blog.length, 2);
		Arrays.sort(answer.blog);
		
		assertEquals(answer.blog[0].postId, 1);
		assertEquals(answer.blog[0].title, "titolo1");
		assertEquals(answer.blog[0].author, "user");
		
		assertEquals(answer.blog[1].postId, 2);
		assertEquals(answer.blog[1].title, "titolo1");
		assertEquals(answer.blog[1].author, "user");
	}
}

package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.GetFeedRequest;
import winsome.connection.socket_messages.server.GetFeedAnswer;
import winsome.server_app.internal.tasks.impl.GetFeedTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_GetFeedTask extends SocketTaskTest
{
	GetFeedTask task;
	GetFeedRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new GetFeedRequest();
		task = new GetFeedTask(state, data, message);
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "Laura", UserFactory.makeNewUser("Laura", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "Nicola", UserFactory.makeNewUser("Nicola", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().get("user").addFollowing("Laura");
		data.getUsers().get("Laura").addFollower("user");
		data.getUsers().get("user").addFollowing("Nicola");
		data.getUsers().get("Nicola").addFollower("user");
		
		GenericPost post1 = data.getPostFactory().makeNewPostId(1, "titolo1", "Laura", "content1");
		GenericPost post2 = data.getPostFactory().makeRewinPostId(2, post1.postId, "Laura");
		GenericPost post3 = data.getPostFactory().makeNewPostId(3, "titolo3", "Nicola", "content3");
		data.getPosts().put(post1.postId, post1);
		data.getPosts().put(post2.postId, post2);
		data.getPosts().put(post3.postId, post3);
		data.getUsers().get("Laura").addPost(post1.postId);
		data.getUsers().get("Laura").addPost(post2.postId);
		data.getUsers().get("Nicola").addPost(post3.postId);
		state.set_user_called = true;
	}
	
	@Test
	void testNormalOperation()
	{
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == GetFeedAnswer.class);
		GetFeedAnswer answer = (GetFeedAnswer) state.sent_message;
		assertEquals(answer.feed.length, 3);
		Arrays.sort(answer.feed);
		
		assertEquals(answer.feed[0].postId, 1);
		assertEquals(answer.feed[0].title, "titolo1");
		assertEquals(answer.feed[0].author, "Laura");
		
		assertEquals(answer.feed[1].postId, 2);
		assertEquals(answer.feed[1].title, "titolo1");
		assertEquals(answer.feed[1].author, "Laura");
		
		assertEquals(answer.feed[2].postId, 3);
		assertEquals(answer.feed[2].title, "titolo3");
		assertEquals(answer.feed[2].author, "Nicola");
	}
}

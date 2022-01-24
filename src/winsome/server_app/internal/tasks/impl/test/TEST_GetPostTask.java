package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.Post;
import winsome.connection.socket_messages.client.GetPostRequest;
import winsome.connection.socket_messages.server.GetPostAnswer;
import winsome.server_app.internal.tasks.impl.GetPostTask;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_GetPostTask extends SocketTaskTest
{
	GetPostTask task;
	GetPostRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "Nicola", UserFactory.makeNewUser("Nicola", "pass", new Tag[]{ new Tag("tag1") }) );
		
		GenericPost post = data.getPostFactory().makeNewPost("A Cartesio", "Nicola", "Ho sconfitto il genio maligno!");
		post.addLike("user");
		post.addComment("user", "Sei un animale!");
		data.getPosts().put(post.postId, post);
		data.getUsers().get("Nicola").addPost(post.postId);
		
		message = new GetPostRequest(post.postId);
		task = new GetPostTask(state, data, message);
		
		state.set_user_called = true;
	}
	
	@Test
	void testNormalOperation()
	{
		task.run(pool);
		
		assertTrue(state.sent_message.getClass() == GetPostAnswer.class);
		Post post = ((GetPostAnswer)state.sent_message).post;
		assertEquals(post.postId, 1);
		assertEquals(post.author, "Nicola");
		assertEquals(post.title, "A Cartesio");
		assertEquals(post.content, "Ho sconfitto il genio maligno!");
		assertEquals(post.positive_ratings, 1);
		assertEquals(post.negative_ratings, 0);
		assertEquals(post.comments.size(), 1);
		assertEquals(post.comments.get(0).username, "user");
		assertEquals(post.comments.get(0).comment, "Sei un animale!");
	}
}

package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.FollowUserRequest;
import winsome.connection.socket_messages.server.FollowUserAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.FollowUserTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_FollowUserTask extends SocketTaskTest
{
	FollowUserTask task;
	FollowUserRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new FollowUserRequest("user_to_follow");
		task = new FollowUserTask(state, data, message);
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "user_to_follow", UserFactory.makeNewUser("user_to_follow", "pass", new Tag[]{ new Tag("tag1") }) );
		state.set_user_called = true;
	}

	@Test
	void testNormalOperation()
	{
		assertFalse(data.getUsers().get("user").getFollowing().contains("user_to_follow"));
		assertFalse(data.getUsers().get("user_to_follow").getFollowers().contains("user"));
		
		task.run(pool);
		
		assertTrue(data.getFollowerUpdater().newFollowerCalled);
		assertFalse(data.getFollowerUpdater().removeFollowerCalled);
		data.getFollowerUpdater().checkNotifyForUserAboutUser("user_to_follow", "user");
		
		assertTrue(data.getUsers().get("user").getFollowing().contains("user_to_follow"));
		assertTrue(data.getUsers().get("user_to_follow").getFollowers().contains("user"));
		assertTrue(state.sent_message.getClass() == FollowUserAnswer.class);
	}

	@Test
	void testOnUserAlreadyFollowed()
	{
		data.getUsers().get("user").addFollowing("user_to_follow");
		data.getUsers().get("user_to_follow").addFollower("user");

		task.run(pool);
		
		assertFalse(data.getFollowerUpdater().newFollowerCalled);
		assertFalse(data.getFollowerUpdater().removeFollowerCalled);
		assertTrue(data.getUsers().get("user").getFollowing().contains("user_to_follow"));
		assertTrue(data.getUsers().get("user_to_follow").getFollowers().contains("user"));
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

	@Test
	void testOnFollowingSelf()
	{
		message = new FollowUserRequest("user");
		task = new FollowUserTask(state, data, message);

		task.run(pool);
		
		assertFalse(data.getFollowerUpdater().newFollowerCalled);
		assertFalse(data.getFollowerUpdater().removeFollowerCalled);
		assertFalse(data.getUsers().get("user").getFollowing().contains("user"));
		assertFalse(data.getUsers().get("user").getFollowers().contains("user"));
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

}

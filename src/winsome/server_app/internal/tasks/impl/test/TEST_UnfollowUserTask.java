package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.connection.socket_messages.client.UnfollowUserRequest;
import winsome.connection.socket_messages.server.UnfollowUserAnswer;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;
import winsome.server_app.internal.tasks.impl.UnfollowUserTask;
import winsome.server_app.user.Tag;
import winsome.server_app.user.UserFactory;

class TEST_UnfollowUserTask extends SocketTaskTest
{
	UnfollowUserTask task;
	UnfollowUserRequest message;
	
	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		
		message = new UnfollowUserRequest("user_to_follow");
		task = new UnfollowUserTask(state, data, message);
		
		data.getUsers().put( "user", UserFactory.makeNewUser("user", "pass", new Tag[]{ new Tag("tag1") }) );
		data.getUsers().put( "user_to_follow", UserFactory.makeNewUser("user_to_follow", "pass", new Tag[]{ new Tag("tag1") }) );
		state.set_user_called = true;
	}

	@Test
	void testNormalOperation()
	{
		data.getUsers().get("user").addFollowing("user_to_follow");
		data.getUsers().get("user_to_follow").addFollower("user");
		
		task.run(pool);
		
		assertTrue(data.getFollowerUpdater().removeFollowerCalled);
		assertFalse(data.getFollowerUpdater().newFollowerCalled);
		data.getFollowerUpdater().checkNotifyForUserAboutUser("user_to_follow", "user");

		assertFalse(data.getUsers().get("user").getFollowing().contains("user_to_follow"));
		assertFalse(data.getUsers().get("user_to_follow").getFollowers().contains("user"));
		assertTrue(state.sent_message.getClass() == UnfollowUserAnswer.class);
	}

	@Test
	void testOnUserNotFollowed()
	{
		assertFalse(data.getUsers().get("user").getFollowing().contains("user_to_follow"));
		assertFalse(data.getUsers().get("user_to_follow").getFollowers().contains("user"));

		task.run(pool);
		
		assertFalse(data.getFollowerUpdater().newFollowerCalled);
		assertFalse(data.getFollowerUpdater().removeFollowerCalled);
		assertFalse(data.getUsers().get("user").getFollowing().contains("user_to_follow"));
		assertFalse(data.getUsers().get("user_to_follow").getFollowers().contains("user"));
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

	@Test
	void testOnUnfollowingSelf()
	{
		message = new UnfollowUserRequest("user");
		task = new UnfollowUserTask(state, data, message);

		task.run(pool);
		
		assertFalse(data.getFollowerUpdater().newFollowerCalled);
		assertFalse(data.getFollowerUpdater().removeFollowerCalled);
		assertFalse(data.getUsers().get("user").getFollowing().contains("user"));
		assertFalse(data.getUsers().get("user").getFollowers().contains("user"));
		assertTrue(state.sent_message.getClass() == RequestExceptionAnswer.class);
	}

}

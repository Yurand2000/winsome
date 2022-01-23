package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.NotFollowingUserException;
import winsome.client_app.internal.tasks.UnfollowUserExecutor;
import winsome.connection.socket_messages.client.UnfollowUserRequest;
import winsome.connection.socket_messages.server.UnfollowUserAnswer;

class TEST_UnfollowUserExecutor extends TaskExecutorTest
{
	private UnfollowUserExecutor task;
	private String who_to_follow;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		who_to_follow = "Luigi";
		task = new UnfollowUserExecutor(who_to_follow);
	}

	@Test
	void testNormalOperation()
	{
		UnfollowUserAnswer answer = new UnfollowUserAnswer();
		connection.setReceiveMessage(answer);
		app_api.getFollowing().add("Luigi");
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == UnfollowUserRequest.class);
		assertEquals( ((UnfollowUserRequest)connection.sent_message).username, "Luigi" );
		assertFalse(app_api.getFollowing().contains("Luigi"));
	}

	@Test
	void testThrowsIfAlreadyFollowing()
	{
		UnfollowUserAnswer answer = new UnfollowUserAnswer();
		connection.setReceiveMessage(answer);
		assertFalse(app_api.getFollowing().contains("Luigi"));
		
		assertThrows(NotFollowingUserException.class, () -> task.run(connection, app_api));
		
		assertTrue(connection.sent_message == null);
	}

}

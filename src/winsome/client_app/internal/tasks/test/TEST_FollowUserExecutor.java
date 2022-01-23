package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.AlreadyFollowingUserException;
import winsome.client_app.internal.tasks.FollowUserExecutor;
import winsome.connection.socket_messages.client.FollowUserRequest;
import winsome.connection.socket_messages.server.FollowUserAnswer;

class TEST_FollowUserExecutor extends TaskExecutorTest
{
	private FollowUserExecutor task;
	private String who_to_follow;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		who_to_follow = "Luigi";
		task = new FollowUserExecutor(who_to_follow);
	}

	@Test
	void testNormalOperation()
	{
		FollowUserAnswer answer = new FollowUserAnswer();
		connection.setReceiveMessage(answer);
		assertFalse(app_api.getFollowing().contains("Luigi"));
		
		task.run(connection, app_api);
		
		assertTrue(connection.sent_message.getClass() == FollowUserRequest.class);
		assertEquals( ((FollowUserRequest)connection.sent_message).username, "Luigi" );
		assertTrue(app_api.getFollowing().contains("Luigi"));
	}

	@Test
	void testThrowsIfAlreadyFollowing()
	{
		FollowUserAnswer answer = new FollowUserAnswer();
		connection.setReceiveMessage(answer);
		app_api.getFollowing().add("Luigi");
		
		assertThrows(AlreadyFollowingUserException.class, () -> task.run(connection, app_api));
		
		assertTrue(connection.sent_message == null);
	}

}

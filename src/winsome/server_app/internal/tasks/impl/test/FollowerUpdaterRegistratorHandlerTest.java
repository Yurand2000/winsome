package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;

class FollowerUpdaterRegistratorHandlerTest implements FollowerUpdaterRegistratorHandler
{
	private String user;
	private String follower;
	public boolean newFollowerCalled = false;
	public boolean removeFollowerCalled = false;

	@Override
	public void notifyNewFollower(String user, String new_follower)
	{
		this.user = user;
		this.follower = new_follower;
		newFollowerCalled = true;
	}

	@Override
	public void notifyRemovedFollower(String user, String removed_follower)
	{
		this.user = user;
		this.follower = removed_follower;
		removeFollowerCalled = true;
	}
	
	public void checkNotifyForUserAboutUser(String user, String follower)
	{
		assertEquals(this.user, user);
		assertEquals(this.follower, follower);
	}

}

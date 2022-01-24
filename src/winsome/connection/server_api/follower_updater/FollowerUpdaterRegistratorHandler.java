package winsome.connection.server_api.follower_updater;

public interface FollowerUpdaterRegistratorHandler
{
	void notifyNewFollower(String user, String new_follower);
	void notifyRemovedFollower(String user, String removed_follower);
}

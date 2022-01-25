package winsome.connection.server_api.follower_updater;

import winsome.connection.protocols.FollowerUpdaterRMI;
import winsome.connection.server_api.RMIObjectRegistrator;

public class FollowerUpdaterRegistratorHandlerImpl extends RMIObjectRegistrator<FollowerUpdaterRegistratorImpl> implements FollowerUpdaterRegistratorHandler
{	
	public FollowerUpdaterRegistratorHandlerImpl(Integer registry_port)
	{
		super( new FollowerUpdaterRegistratorImpl(), FollowerUpdaterRMI.getFollowerUpdaterRegistratorName(), registry_port );
	}
	
	@Override
	public void notifyNewFollower(String user, String new_follower)
	{
		object.notifyNewFollower(user, new_follower);
	}
	
	@Override
	public void notifyRemovedFollower(String user, String removed_follower)
	{
		object.notifyRemovedFollower(user, removed_follower);
	}
}

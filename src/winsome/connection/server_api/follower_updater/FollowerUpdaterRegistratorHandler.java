package winsome.connection.server_api.follower_updater;

import winsome.connection.protocols.FollowerUpdaterRMI;
import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.connection.server_api.RMIObjectRegistrator;

public class FollowerUpdaterRegistratorHandler extends RMIObjectRegistrator<FollowerUpdaterRegistratorImpl>
{	
	public FollowerUpdaterRegistratorHandler()
	{
		super( new FollowerUpdaterRegistratorImpl(), FollowerUpdaterRMI.getFollowerUpdaterRegistratorName(), WinsomeConnectionProtocol.getRMIRegistryPort() );
	}
	
	public void notifyNewFollower(String user, String new_follower)
	{
		object.notifyNewFollower(user, new_follower);
	}
	
	public void notifyRemovedFollower(String user, String removed_follower)
	{
		object.notifyRemovedFollower(user, removed_follower);
	}
}

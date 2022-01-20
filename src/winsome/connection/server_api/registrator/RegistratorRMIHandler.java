package winsome.connection.server_api.registrator;

import winsome.connection.protocols.RegistratorRMI;
import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.connection.server_api.RMIObjectRegistrator;
import winsome.server_app.internal.WinsomeServer;

public class RegistratorRMIHandler extends RMIObjectRegistrator<RegistratorImpl>
{	
	public RegistratorRMIHandler(WinsomeServer server)
	{
		super( new RegistratorImpl(server), RegistratorRMI.getRegistratorName(), WinsomeConnectionProtocol.getRMIRegistryPort() );
	}
	
	public RegistratorImpl getRegistrator()
	{
		return object;
	}
}

package winsome.connection.server_api.registrator;

import winsome.connection.protocols.RegistratorRMI;
import winsome.connection.server_api.RMIObjectRegistrator;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class RegistratorRMIHandler extends RMIObjectRegistrator<RegistratorImpl>
{	
	public RegistratorRMIHandler(WinsomeData winsome_data, ServerThreadpool pool, Integer registry_port)
	{
		super( new RegistratorImpl(winsome_data, pool), RegistratorRMI.getRegistratorName(), registry_port );
	}
}

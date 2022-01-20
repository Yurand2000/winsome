package winsome.client_app.internal;

import java.net.InetSocketAddress;
import winsome.client_app.api.ApplicationAPI;
import winsome.client_app.api.LoggedClientAPI;
import winsome.client_app.api.User;
import winsome.connection.client_api.registrator.RegistratorRMIHandler;

public class ApplicationAPIImpl implements ApplicationAPI
{
	private final InetSocketAddress server_address;
	private LoggedClientAPI logged_client;
	
	public ApplicationAPIImpl(InetSocketAddress server_address)
	{
		this.server_address = server_address;
		this.logged_client = null;
	}

	@Override
	public void register(String username, String password, User.Tag[] tags)
	{
		String[] converted_tags = new String[tags.length];
		for(int i = 0; i < tags.length; i++)
			converted_tags[i] = tags[i].toString();
		
		RegistratorRMIHandler.register(server_address.getHostString(), username, password, converted_tags);
	}

	@Override
	public void login(String username, String password)
	{
		
	}

	@Override
	public void logout()
	{
		
	}

	@Override
	public LoggedClientAPI getLoggedAPI()
	{
		return logged_client;
	}

}

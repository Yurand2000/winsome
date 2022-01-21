package winsome.client_app;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import winsome.connection.protocols.WinsomeConnectionProtocol;

public class ClientSettings
{
	public final String server_address;
	
	public ClientSettings()
	{
		server_address = "localhost";
	}
	
	public ClientSettings(String server_address)
	{
		this.server_address = server_address;
	}
	
	public InetSocketAddress makeServerAdddress() throws UnknownHostException
	{
		return InetSocketAddress.createUnresolved(server_address, WinsomeConnectionProtocol.getTCPListenerPort());
	}
}

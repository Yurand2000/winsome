package winsome.client_app;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.connection.protocols.WinsomeConnectionProtocol;
import winsome.generic.SettingsReader;

public class ClientSettings extends SettingsReader
{
	@JsonProperty() public final String server_address;
	
	public ClientSettings()
	{
		server_address = "127.0.0.1";
	}
	
	public static ClientSettings deserializeFromFile(String filename)
	{
		return deserializeFromFile(ClientSettings.class, filename);
	}
	
	public InetSocketAddress makeServerAdddress() throws UnknownHostException
	{
		return new InetSocketAddress(InetAddress.getByName(server_address), WinsomeConnectionProtocol.getTCPListenerPort());
	}
}

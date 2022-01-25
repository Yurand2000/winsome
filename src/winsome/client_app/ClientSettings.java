package winsome.client_app;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.generic.SettingsReader;

public class ClientSettings extends SettingsReader
{
	@JsonProperty() public final String server_address;
	@JsonProperty() public final Integer server_port;
	@JsonProperty() public final Integer server_rmi_port;
	
	public ClientSettings()
	{
		server_address = "127.0.0.1";
		server_port = 8080;
		server_rmi_port = 8081;
	}
	
	public static ClientSettings deserializeFromFile(String filename)
	{
		return deserializeFromFile(ClientSettings.class, filename);
	}
	
	public InetSocketAddress makeServerAdddress() throws UnknownHostException
	{
		return new InetSocketAddress(InetAddress.getByName(server_address), server_port);
	}
}

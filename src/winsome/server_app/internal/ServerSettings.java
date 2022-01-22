package winsome.server_app.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.generic.SettingsReader;

public class ServerSettings extends SettingsReader
{
	@JsonProperty() public final String server_address;
	@JsonProperty() public final String server_udp_address;
	@JsonProperty() public final String save_file;
	
	public ServerSettings()
	{
		server_address = "127.0.0.1";
		server_udp_address = "224.0.0.128";
		save_file = "winsome_data.json";
	}
	
	public static ServerSettings deserializeFromFile(String filename)
	{
		return deserializeFromFile(ServerSettings.class, filename);
	}
}

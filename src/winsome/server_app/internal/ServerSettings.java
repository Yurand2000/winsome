package winsome.server_app.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import winsome.generic.SettingsReader;

public class ServerSettings extends SettingsReader
{
	@JsonProperty() public final String server_address;
	@JsonProperty() public final Integer server_port;
	@JsonProperty() public final Integer server_rmi_port;
	@JsonProperty() public final String server_udp_address;
	@JsonProperty() public final Integer server_udp_port;
	@JsonProperty() public final String save_file;
	@JsonProperty() public final Integer reward_timer_in_seconds;
	@JsonProperty() public final String reward_author_part;
	
	public ServerSettings()
	{
		server_address = "127.0.0.1";
		server_port = 8080;
		server_rmi_port = 8081;
		server_udp_address = "224.0.0.128";
		server_udp_port = 8082;
		save_file = "winsome_data.json";
		reward_timer_in_seconds = 1800;
		reward_author_part = "0.7";
	}
	
	public static ServerSettings deserializeFromFile(String filename)
	{
		return deserializeFromFile(ServerSettings.class, filename);
	}
}

package winsome.connection.server_api.socket;

public interface SocketState
{
	String getSocketUser();
	void setSocketUser(String username);
	SocketReader getReader();
	SocketWriter getWriter();
}

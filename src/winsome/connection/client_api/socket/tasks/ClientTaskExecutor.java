package winsome.connection.client_api.socket.tasks;

import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;

public interface ClientTaskExecutor
{
	public void run(ConnectionHandler connection, ApplicationLoggedAPI logged_api);
}

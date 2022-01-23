package winsome.client_app.internal.tasks;

import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;

public interface ClientTaskExecutor
{
	public void run(ConnectionHandler connection, ApplicationLoggedAPI logged_api);
}

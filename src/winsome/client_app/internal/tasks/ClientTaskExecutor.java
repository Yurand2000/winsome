package winsome.client_app.internal.tasks;

import winsome.client_app.internal.ApplicationLoggedAPIImpl;
import winsome.client_app.internal.ConnectionHandler;

public interface ClientTaskExecutor
{
	public void run(ConnectionHandler connection, ApplicationLoggedAPIImpl logged_api);
}

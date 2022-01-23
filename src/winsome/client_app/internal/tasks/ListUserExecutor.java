package winsome.client_app.internal.tasks;

import java.io.IOException;
import java.util.List;

import winsome.client_app.api.User;
import winsome.connection.client_api.socket.ApplicationLoggedAPI;
import winsome.connection.client_api.socket.ConnectionHandler;
import winsome.connection.socket_messages.client.ListUserRequest;
import winsome.connection.socket_messages.server.ListUserAnswer;

public class ListUserExecutor extends DefaultTaskExecutor
{
	private final List<User> users;
	
	public ListUserExecutor(List<User> users)
	{
		this.users = users;
	}

	@Override
	protected void execute(ConnectionHandler connection, ApplicationLoggedAPI api) throws IOException
	{
		ListUserRequest request = new ListUserRequest();
		connection.sendMessage(request);
		
		ListUserAnswer answer = connection.readMessage(ListUserAnswer.class);
		copyUsersIntoSuppliedList(answer.similar_users);
	}
	
	private void copyUsersIntoSuppliedList(String[] similar_users)
	{
		for(String username : similar_users)
		{
			User u = new User(username);
			users.add(u);
		}
	}
}

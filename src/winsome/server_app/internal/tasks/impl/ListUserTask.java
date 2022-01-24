package winsome.server_app.internal.tasks.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import winsome.connection.socket_messages.client.ListUserRequest;
import winsome.connection.socket_messages.server.ListUserAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.Tag;
import winsome.server_app.user.User;

public class ListUserTask extends LoggedUserTask
{
	@SuppressWarnings("unused")
	private final ListUserRequest message;

	public ListUserTask(SocketTaskState socket, WinsomeData data, ListUserRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		List<String> similar_users = new ArrayList<String>();
		User user = getCurrentUser();
		
		for(User u : data.getUsers().values())
		{
			if(u.username != user.username && hasAtLeastOneTagInCommon(user, u))
			{
				similar_users.add(u.username);
			}
		}
		
		ListUserAnswer answer = new ListUserAnswer(similar_users.toArray(new String[0]));
		socket.sendAnswerMessage(answer);
	}
	
	private boolean hasAtLeastOneTagInCommon(User me, User other)
	{
		boolean tag_in_common = false;
		List<Tag> tags = me.tags;
		
		Iterator<Tag> it = other.tags.iterator();
		while(!tag_in_common && it.hasNext())
		{
			if(tags.contains(it.next()))
			{
				tag_in_common = true;
			}
		}		
		return tag_in_common;
	}
}

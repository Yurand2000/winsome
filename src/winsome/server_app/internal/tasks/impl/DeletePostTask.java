package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.DeletePostRequest;
import winsome.connection.socket_messages.server.DeletePostAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.user.User;

public class DeletePostTask extends LoggedUserTask
{
	private final DeletePostRequest message;

	public DeletePostTask(SocketTaskState socket, WinsomeData data, DeletePostRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		checkUserOwnsPost(user, message.postId);			
		TaskUtils.deletePost(message.postId, data);
		
		socket.sendAnswerMessage(new DeletePostAnswer());
	}
	
	private void checkUserOwnsPost(User user, Integer postId)
	{		
		if(!user.username.equals(TaskUtils.getPostAuthor(postId, data)))
		{
			throw new RuntimeException("Given post with id " + postId.toString() + " is not owned by the current user.");
		}
	}
}

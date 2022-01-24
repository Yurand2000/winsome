package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.CreatePostRequest;
import winsome.connection.socket_messages.server.CreatePostAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

public class CreatePostTask extends LoggedUserTask
{
	private final CreatePostRequest message;

	public CreatePostTask(SocketTaskState socket, WinsomeData data, CreatePostRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	protected void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		checkPostContent();
		makeNewPost(user);
	}

	private void checkPostContent()
	{
		if(message.title.length() > 20)
		{
			throw new RuntimeException("Cannot create post: title too long (max 20 character)");
		}
		
		if(message.content.length() > 500)
		{
			throw new RuntimeException("Cannot create post: content too long (max 500 character)");
		}
	}
	
	private void makeNewPost(User user)
	{
		GenericPost post = data.getPostFactory().makeNewPost(message.title, user.username, message.content);
		
		TaskUtils.lockUserThenPost(user, post, () ->
		{
			user.addPost(post.postId);
			data.getPosts().put(post.postId, post);
		});
		
		socket.sendAnswerMessage(new CreatePostAnswer(post.postId));
	}
}

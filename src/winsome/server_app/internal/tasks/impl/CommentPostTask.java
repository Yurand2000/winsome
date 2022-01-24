package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.CommentPostRequest;
import winsome.connection.socket_messages.server.CommentPostAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

public class CommentPostTask extends LoggedUserTask
{
	private final CommentPostRequest message;

	public CommentPostTask(SocketTaskState socket, WinsomeData data, CommentPostRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	protected void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		checkUserIsFollowingPostAuthor(user, message.postId);
		
		GenericPost post = TaskUtils.getPost(message.postId, data);
		TaskUtils.lockPost(post, () ->
		{
			post.addComment(user.username, message.comment);
		});
		
		CommentPostAnswer answer = new CommentPostAnswer();
		socket.sendAnswerMessage(answer);
	}
	
	private void checkUserIsFollowingPostAuthor(User user, Integer postId)
	{
		String post_author = TaskUtils.getPostAuthor(postId, data);
		
		if(user.username == post_author)
		{
			throw new RuntimeException("Cannot rate your own post.");
		}
		
		TaskUtils.lockUser(user, () ->
		{			
			if(!user.getFollowing().contains(post_author))
			{
				throw new RuntimeException("The user is not following the author of the post to rewin.");
			}
		});
	}
}

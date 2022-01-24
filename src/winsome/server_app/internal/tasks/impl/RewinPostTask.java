package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.client.RewinPostRequest;
import winsome.connection.socket_messages.server.RewinPostAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

public class RewinPostTask extends LoggedUserTask
{
	private final RewinPostRequest message;

	public RewinPostTask(SocketTaskState socket, WinsomeData data, RewinPostRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		checkUserIsFollowingPostAuthor(user, message.postId);
		
		GenericPost post = data.getPostFactory().makeRewinPost(message.postId, user.username);
		GenericPost original_post = TaskUtils.getPost(message.postId, data);
		
		TaskUtils.lockUserThenPosts(user, post, original_post, () ->
		{
			user.addPost(post.postId);
			data.getPosts().put(post.postId, post);
			original_post.addRewin(post.postId);
		});
		
		RewinPostAnswer answer = new RewinPostAnswer(post.postId, TaskUtils.getPostContent(original_post.postId, data).title);
		socket.sendAnswerMessage(answer);
	}
	
	private void checkUserIsFollowingPostAuthor(User user, Integer postId)
	{
		String post_author = TaskUtils.getPostAuthor(postId, data);
		
		TaskUtils.lockUser(user, () ->
		{
			if(!user.getFollowing().contains(post_author))
			{
				throw new RuntimeException("The user is not following the author of the post to rewin.");
			}
		});
	}
}

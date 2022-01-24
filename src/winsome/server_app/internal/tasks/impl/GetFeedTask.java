package winsome.server_app.internal.tasks.impl;

import java.util.ArrayList;
import java.util.List;

import winsome.client_app.api.PostShort;
import winsome.connection.socket_messages.client.GetFeedRequest;
import winsome.connection.socket_messages.server.GetFeedAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.Content;
import winsome.server_app.user.User;

public class GetFeedTask extends LoggedUserTask
{
	@SuppressWarnings("unused")
	private final GetFeedRequest message;

	public GetFeedTask(SocketTaskState socket, WinsomeData data, GetFeedRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		List<String> following = user.getFollowing();
		List<Integer> feed_postIds = new ArrayList<Integer>();
		List<PostShort> feed_posts = new ArrayList<PostShort>();
		
		getFeedPostIds(following, feed_postIds);
		getFeedPosts(feed_postIds, feed_posts);
		
		GetFeedAnswer answer = new GetFeedAnswer(feed_posts);
		socket.sendAnswerMessage(answer);
	}
	
	private void getFeedPostIds(List<String> following, List<Integer> feed_posts)
	{
		for(String username : following)
		{
			User followed = TaskUtils.getUser(username, data);
			TaskUtils.lockUser(followed, () ->
			{
				for(Integer postId : followed.getPosts())
				{
					feed_posts.add(postId);
				}
			});
		}
	}
	
	private void getFeedPosts(List<Integer> feed_postIds, List<PostShort> feed_posts)
	{
		for(Integer id : feed_postIds)
		{
			try
			{
				Content post_content = TaskUtils.getPostContent(id, data);
				PostShort post_short = new PostShort(id, post_content.author, post_content.title);
				feed_posts.add(post_short);
			}
			catch(RuntimeException e) { /*probably the post has been deleted while trying to fetch it.*/ }
		}
	}
}

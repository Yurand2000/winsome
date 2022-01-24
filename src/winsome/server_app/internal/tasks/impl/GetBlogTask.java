package winsome.server_app.internal.tasks.impl;

import java.util.ArrayList;
import java.util.List;

import winsome.client_app.api.PostShort;
import winsome.connection.socket_messages.client.GetBlogRequest;
import winsome.connection.socket_messages.server.GetBlogAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.Content;
import winsome.server_app.user.User;

public class GetBlogTask extends LoggedUserTask
{
	@SuppressWarnings("unused")
	private final GetBlogRequest message;

	public GetBlogTask(SocketTaskState socket, WinsomeData data, GetBlogRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{
		User user = getCurrentUser();
		
		List<PostShort> blog_posts = new ArrayList<PostShort>();
		getBlogPosts(user.getPosts(), blog_posts);
		
		GetBlogAnswer answer = new GetBlogAnswer(blog_posts);
		socket.sendAnswerMessage(answer);
	}
	
	private void getBlogPosts(List<Integer> blog_postIds, List<PostShort> blog_posts)
	{
		for(Integer id : blog_postIds)
		{
			try
			{
				Content post_content = TaskUtils.getPostContent(id, data);
				PostShort post_short = new PostShort(id, post_content.author, post_content.title);
				blog_posts.add(post_short);
			}
			catch(RuntimeException e) { /*probably the post has been deleted while trying to fetch it.*/ }
		}
	}
}

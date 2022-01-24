package winsome.server_app.internal.tasks.impl;

import java.util.ArrayList;
import java.util.List;

import winsome.client_app.api.Post;
import winsome.connection.socket_messages.client.GetPostRequest;
import winsome.connection.socket_messages.server.GetPostAnswer;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.tasks.TaskUtils;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.post.Content;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostComments;

public class GetPostTask extends LoggedUserTask
{
	private final GetPostRequest message;

	public GetPostTask(SocketTaskState socket, WinsomeData data, GetPostRequest message)
	{
		super(socket, data);
		this.message = message;
	}

	@Override
	public void executeTask(ServerThreadpool pool)
	{		
		Post client_post = makeClientPost(message.postId);
		GetPostAnswer answer = new GetPostAnswer(client_post);
		socket.sendAnswerMessage(answer);
	}
	
	private Post makeClientPost(Integer postId)
	{
		GenericPost post = TaskUtils.getPost(postId, data);
		Content content = TaskUtils.getPostContent(postId, data);
		
		List<PostComments.Comment> comments = post.getComments();
		Post client_post = new Post(post.postId, content.author, content.title, content.content,
			post.getPositiveRatings(), post.getNegativeRatings(), makeComments(comments));
		
		return client_post;
	}
	
	private List<Post.Comment> makeComments(List<PostComments.Comment> comments)
	{
		List<Post.Comment> api_comments = new ArrayList<Post.Comment>();
		for(PostComments.Comment comment : comments)
		{
			api_comments.add(new Post.Comment(comment.username, comment.comment));
		}
		
		return api_comments;
	}
}

package winsome.server_app.internal.tasks.impl;

import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.client.*;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.internal.tasks.SocketTaskState;
import winsome.server_app.internal.threadpool.ServerThreadpool;

public class ParseIncomingMessageTask extends SocketClientTask
{
	public ParseIncomingMessageTask(SocketTaskState socket, WinsomeData data)
	{
		super(socket, data);
	}
	
	@Override
	public void execute(ServerThreadpool pool)
	{
		Message message = socket.getRequestMessage();
		if(message.getClass() == LoginRequest.class)
		{
			pool.enqueueTask(new LoginUserTask(socket, data, (LoginRequest) message));
		}
		else if(message.getClass() == LogoutRequest.class)
		{
			pool.enqueueTask(new LogoutUserTask(socket, data, (LogoutRequest) message));
		}
		else if(message.getClass() == CommentPostRequest.class)
		{
			pool.enqueueTask(new CommentPostTask(socket, data, (CommentPostRequest) message));
		}
		else if(message.getClass() == CreatePostRequest.class)
		{
			pool.enqueueTask(new CreatePostTask(socket, data, (CreatePostRequest) message));
		}
		else if(message.getClass() == DeletePostRequest.class)
		{
			pool.enqueueTask(new DeletePostTask(socket, data, (DeletePostRequest) message));
		}
		else if(message.getClass() == FollowUserRequest.class)
		{
			pool.enqueueTask(new FollowUserTask(socket, data, (FollowUserRequest) message));
		}
		else if(message.getClass() == GetBlogRequest.class)
		{
			pool.enqueueTask(new GetBlogTask(socket, data, (GetBlogRequest) message));
		}
		else if(message.getClass() == GetFeedRequest.class)
		{
			pool.enqueueTask(new GetFeedTask(socket, data, (GetFeedRequest) message));
		}
		else if(message.getClass() == GetPostRequest.class)
		{
			pool.enqueueTask(new GetPostTask(socket, data, (GetPostRequest) message));
		}
		else if(message.getClass() == GetWalletInBitcoinRequest.class)
		{
			pool.enqueueTask(new GetWalletInBitcoinTask(socket, data, (GetWalletInBitcoinRequest) message));
		}
		else if(message.getClass() == GetWalletRequest.class)
		{
			pool.enqueueTask(new GetWalletTask(socket, data, (GetWalletRequest) message));
		}
		else if(message.getClass() == ListUserRequest.class)
		{
			pool.enqueueTask(new ListUserTask(socket, data, (ListUserRequest) message));
		}
		else if(message.getClass() == RatePostRequest.class)
		{
			pool.enqueueTask(new RatePostTask(socket, data, (RatePostRequest) message));
		}
		else if(message.getClass() == RewinPostRequest.class)
		{
			pool.enqueueTask(new RewinPostTask(socket, data, (RewinPostRequest) message));
		}
		else if(message.getClass() == UnfollowUserRequest.class)
		{
			pool.enqueueTask(new UnfollowUserTask(socket, data, (UnfollowUserRequest) message));
		}
		else
		{
			throw new RuntimeException("Unknown task.");
		}
	}
}

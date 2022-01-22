package winsome.server_app.internal.tasks;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.atomic.AtomicInteger;

import winsome.connection.server_api.socket.SocketState;
import winsome.connection.socket_messages.Message;
import winsome.generic.SerializerWrapper;
import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.Content;
import winsome.server_app.post.ContentPost;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewinPost;

public class TaskUtils
{
	private TaskUtils() { }
	
	public static void setSocketReadyToWrite(SelectionKey key)
	{
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
	}
	
	public static void setSocketReadyToRead(SelectionKey key)
	{
		key.interestOps(SelectionKey.OP_READ);
		key.selector().wakeup();
	}
	
	public static void sendMessage(SocketState infos, Message msg)
	{
		infos.writer.addMessageToSend(serializeMessage(msg));
	}
	
	private static byte[] serializeMessage(Message msg)
	{
		try
		{
			return SerializerWrapper.serializeCompact(msg);
		}
		catch (IOException e)
		{
			return new byte[0];
		}
	}
	
	public static String getPostAuthor(Integer postId, WinsomeData server_data)
	{
		GenericPost post = server_data.posts.get(postId);
		return post.getAuthor();
	}
	
	public static Content getPostContent(Integer postId, WinsomeData server_data)
	{
		String author = getPostAuthor(postId, server_data);
		return getPostContent(postId, author, server_data);
	}
	
	private static Content getPostContent(Integer postId, String author, WinsomeData server_data)
	{
		GenericPost post = server_data.posts.get(postId);
		
		if(post.isRewin())
		{
			AtomicInteger original_postId = new AtomicInteger();
			WinsomeData.lockPost(post, () -> {
				original_postId.set(((RewinPost)post).getOriginalPost());
			});
			
			return getPostContent(original_postId.get(), author, server_data);
		}
		else
		{
			StringBuilder title = new StringBuilder();
			StringBuilder content = new StringBuilder();
			WinsomeData.lockPost(post, () -> {
				title.append( ((ContentPost)post).content.title );
				content.append( ((ContentPost)post).content.content );
			});
			
			return new Content(title.toString(), author, content.toString());
		}
	}
}

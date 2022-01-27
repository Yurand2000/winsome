package winsome.connection.server_api.socket;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import winsome.connection.socket_messages.Message;
import winsome.generic.SerializerWrapper;

public class SocketUtils
{
	private SocketUtils() { }
	
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
	
	public static void sendMessage(SocketState state, Message msg)
	{
		state.getWriter().addMessageToSend(serializeMessage(msg));
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
	
	public static void destroyKey(SelectionKey key) throws IOException
	{
		key.cancel();
		key.channel().close();
	}
}

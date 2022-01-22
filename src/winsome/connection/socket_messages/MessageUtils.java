package winsome.connection.socket_messages;

import winsome.connection.socket_messages.client.*;
import winsome.connection.socket_messages.server.*;
import winsome.generic.SerializerWrapper;

public class MessageUtils
{
	private MessageUtils() { }
	
	public static void registerJsonDeserializers()
	{
		SerializerWrapper.addDeserializers(
			LoginRequest.class,
			LogoutRequest.class
		);
		
		SerializerWrapper.addDeserializers(
			LoginAnswer.class,
			LogoutAnswer.class,
			RequestExceptionAnswer.class
		);
	}
}

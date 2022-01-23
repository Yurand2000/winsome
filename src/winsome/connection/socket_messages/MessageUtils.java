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
			CommentPostRequest.class,
			CreatePostRequest.class,
			DeletePostRequest.class,
			FollowUserRequest.class,
			GetFeedRequest.class,
			GetPostRequest.class,
			GetWalletInBitcoinRequest.class,
			GetWalletRequest.class,
			ListUserRequest.class,
			LoginRequest.class,
			LogoutRequest.class,
			RatePostRequest.class,
			RewinPostRequest.class,
			UnfollowUserRequest.class
		);
		
		SerializerWrapper.addDeserializers(
			CommentPostAnswer.class,
			CreatePostAnswer.class,
			DeletePostAnswer.class,
			FollowUserAnswer.class,
			GetFeedAnswer.class,
			GetPostAnswer.class,
			GetWalletAnswer.class,
			GetWalletInBitcoinAnswer.class,
			ListUserAnswer.class,
			LoginAnswer.class,
			LogoutAnswer.class,
			RatePostAnswer.class,
			RequestExceptionAnswer.class,
			RewinPostAnswer.class,
			UnfollowUserAnswer.class
		);
	}
}

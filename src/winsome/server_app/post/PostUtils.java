package winsome.server_app.post;

import winsome.generic.SerializerWrapper;

public class PostUtils
{
	private PostUtils() { }
	
	public static void registerJsonDeserializers()
	{
		SerializerWrapper.addDeserializers(
			ContentPost.class,
			RewinPost.class
		);
		
		SerializerWrapper.addDeserializers(
			PostCommentsImpl.class,
			PostLikesImpl.class,
			RewardStateImpl.class
		);
	}
}

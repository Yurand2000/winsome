package winsome.server.post;

import java.util.Set;

public class ContentPost extends GenericPost
{
	public final Content content;

	public ContentPost(Integer postId, Content content, Set<Integer> rewins,
		PostLikes likes, PostComments comments, RewardState reward_state)
	{
		super(postId, rewins, likes, comments, reward_state);
		this.content = content;
	}

	public Content getContent()
	{
		return content;
	}
	
	public boolean isRewin()
	{
		return false;
	}
}

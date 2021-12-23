package winsome.server;

import java.util.Set;

public class RewinPost extends GenericPost
{
	private final Integer originalPostId;
	private final String rewinAuthor;

	public RewinPost(Integer postId, Integer originalPostId, String author, Set<Integer> rewins,
		PostLikes likes, PostComments comments, RewardState reward_state)
	{
		super(postId, rewins, likes, comments, reward_state);
		this.originalPostId = originalPostId;
		this.rewinAuthor = author;
	}

	public Integer getOriginalPost()
	{
		return originalPostId;
	}
	
	public String getAuthor()
	{
		return rewinAuthor;
	}
	
	public boolean isRewin()
	{
		return true;
	}
}

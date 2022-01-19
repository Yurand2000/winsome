package winsome.server.post;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("rewin_post")
public class RewinPost extends GenericPost
{
	@JsonProperty() private final Integer originalPostId;
	@JsonProperty() private final String rewinAuthor;

	@SuppressWarnings("unused")
	private RewinPost() { super(); originalPostId = 0; rewinAuthor = null; }
	
	private RewinPost(RewinPost post)
	{
		super(post);
		this.originalPostId = post.originalPostId;
		this.rewinAuthor = post.rewinAuthor;
	}
	
	public RewinPost(Integer postId, Integer originalPostId, String author, Set<Integer> rewins,
		PostLikes likes, PostComments comments, RewardState reward_state)
	{
		super(postId, rewins, likes, comments, reward_state);
		this.originalPostId = originalPostId;
		this.rewinAuthor = author;
	}

	@JsonIgnore()
	public Integer getOriginalPost()
	{
		return originalPostId;
	}

	@Override
	@JsonIgnore()
	public String getAuthor()
	{
		return rewinAuthor;
	}
	
	public boolean isRewin()
	{
		return true;
	}
	
	@Override
	public RewinPost clone()
	{
		return new RewinPost(this);
	}
}

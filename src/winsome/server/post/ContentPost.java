package winsome.server.post;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("content_post")
public class ContentPost extends GenericPost
{
	public final Content content;

	@SuppressWarnings("unused")
	private ContentPost() { super(); content = null; }
	
	private ContentPost(ContentPost post)
	{
		super(post);
		this.content = post.content;
	}
	
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

	@Override
	public ContentPost clone()
	{
		return new ContentPost(this);
	}
}

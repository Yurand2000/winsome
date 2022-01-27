package winsome.server_app.post;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("content_post")
public class ContentPost extends GenericPost
{
	@JsonProperty() public final Content content;

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
	
	@Override
	@JsonIgnore()
	public String getAuthor()
	{
		return content.author;
	}

	@JsonIgnore()
	public Content getContent()
	{
		return content;
	}

	@JsonIgnore()
	@Override
	public boolean isRewin()
	{
		return false;
	}

	@Override
	public synchronized ContentPost clone()
	{
		return new ContentPost(this);
	}
}

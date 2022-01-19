package winsome.server.post.test.genericPost;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.server.post.GenericPost;
import winsome.server.post.PostComments;
import winsome.server.post.PostLikes;
import winsome.server.post.RewardState;

@JsonTypeName("generic_post_test")
public class GenericPostTestImpl extends GenericPost
{
	@SuppressWarnings("unused")
	private GenericPostTestImpl() { super(); }
	
	private GenericPostTestImpl(GenericPostTestImpl post) { super(post); }
	
	public GenericPostTestImpl(Integer postId, Set<Integer> rewins, PostLikes likes, PostComments comments, RewardState reward_state)
	{
		super(postId, rewins, likes, comments, reward_state);
	}

	@Override
	public boolean isRewin() {
		return false;
	}
	
	@Override
	public GenericPostTestImpl clone()
	{
		return new GenericPostTestImpl(this);
	}
}

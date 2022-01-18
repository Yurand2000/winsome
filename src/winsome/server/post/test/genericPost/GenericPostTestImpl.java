package winsome.server.post.test.genericPost;

import java.util.Set;
import winsome.server.post.GenericPost;
import winsome.server.post.PostComments;
import winsome.server.post.PostLikes;
import winsome.server.post.RewardState;

public class GenericPostTestImpl extends GenericPost
{
	public GenericPostTestImpl(Integer postId, Set<Integer> rewins, PostLikes likes, PostComments comments, RewardState reward_state)
	{
		super(postId, rewins, likes, comments, reward_state);
	}

	@Override
	public boolean isRewin() {
		return false;
	}

}

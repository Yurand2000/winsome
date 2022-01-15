package winsome.server.test.genericPost;

import java.util.Set;
import winsome.server.GenericPost;
import winsome.server.PostComments;
import winsome.server.PostLikes;
import winsome.server.RewardState;

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

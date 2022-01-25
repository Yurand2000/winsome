package winsome.server_app.internal.tasks.wallet_update.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;

import winsome.server_app.post.GenericPost;
import winsome.server_app.post.RewardState.Reward;

class GenericPostTest extends GenericPost
{
	public GenericPostTest() { super(); }
	
	public GenericPostTest(Integer postId)
	{
		super(postId, new HashSet<Integer>(), null, null, null);
	}
	
	@Override public boolean isRewin() { fail(); return false; }
	@Override public GenericPost clone() { fail(); return null; }
	
	public boolean getAuthor_called = false;
	
	@Override public String getAuthor()
	{
		getAuthor_called = true;
		return "author";
	}	

	public Reward post_calculated_reward = null;
	public boolean calculateReward_called = false;
	
	@Override
	public Reward calculateReward()
	{
		calculateReward_called = true;
		return post_calculated_reward;
	}
}

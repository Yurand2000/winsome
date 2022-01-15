package winsome.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import winsome.server.exceptions.CannotRateException;

public abstract class GenericPost
{
	private final Integer postId;
	private final Set<Integer> rewins;
	private final PostLikes likes;
	private final PostComments comments;
	private final RewardState reward_state;
	
	public GenericPost(Integer postId, Set<Integer> rewins,
		PostLikes likes, PostComments comments, RewardState reward_state)
	{
		this.postId = postId;
		this.rewins = new HashSet<Integer>(rewins);
		this.likes = likes;
		this.comments = comments;
		this.reward_state = reward_state;
	}
	
	public Integer getPostId()
	{
		return postId;
	}
	
	public abstract boolean isRewin();
	
	public Set<Integer> getRewins()
	{
		return Collections.unmodifiableSet(rewins);
	}
	
	
	
	public void addLike(String username)
	{
		checkCanRate(username);
		likes.addLike(username);
		reward_state.addLike(username);
	}
	
	public void addDislike(String username)
	{
		checkCanRate(username);
		likes.addDislike(username);
		reward_state.addDislike();
	}
	
	private void checkCanRate(String username)
	{
		if(!likes.canRate(username))
		{
			throw new CannotRateException();
		}
	}
	
	public void addComment(String username, String comment)
	{
		comments.addComment(username, comment);
		reward_state.addComment(username);
	}
	
	public void addRewin(Integer postId)
	{
		rewins.add(postId);
	}
	
	public void removeRewin(Integer postId)
	{
		rewins.remove(postId);
	}
}

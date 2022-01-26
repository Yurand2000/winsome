package winsome.server_app.post;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import winsome.server_app.post.PostComments.Comment;
import winsome.server_app.post.RewardState.Reward;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public abstract class GenericPost implements Cloneable
{
	@JsonProperty() public final Integer postId;
	@JsonProperty() private final Set<Integer> rewins;
	@JsonProperty() private final PostLikes likes;
	@JsonProperty() private final PostComments comments;
	@JsonProperty() private final RewardState reward_state;
	@JsonIgnore() private boolean markedForDeletion;
	
	protected GenericPost() { postId = 0; rewins = null; likes = null; comments = null; reward_state = null; markedForDeletion = false; }
	
	protected GenericPost(GenericPost post)
	{
		this.postId = post.postId;
		this.rewins = new HashSet<Integer>(post.rewins);
		this.likes = post.likes.clone();
		this.comments = post.comments.clone();
		this.reward_state = post.reward_state.clone();
		markedForDeletion = false;
	}
	
	public GenericPost(Integer postId, Set<Integer> rewins,
		PostLikes likes, PostComments comments, RewardState reward_state)
	{
		this.postId = postId;
		this.rewins = new HashSet<Integer>(rewins);
		this.likes = likes;
		this.comments = comments;
		this.reward_state = reward_state;
		markedForDeletion = false;
	}
	
	@JsonIgnore() public abstract boolean isRewin();
	
	@JsonIgnore() public abstract String getAuthor();
	
	@JsonIgnore() public boolean isNotMarkedForDeletion()
	{
		return !markedForDeletion;		
	}
	
	@JsonIgnore() public void markForDeletion()
	{
		markedForDeletion = true;
	}

	public abstract GenericPost clone();
	
	@JsonIgnore() public synchronized Set<Integer> getRewins()
	{
		return new HashSet<Integer>(rewins);
	}	
	
	public synchronized void addLike(String username)
	{
		likes.addLike(username);
		reward_state.addLike(username);
	}
	
	public synchronized void addDislike(String username)
	{
		likes.addDislike(username);
		reward_state.addDislike();
	}
	
	public synchronized void addComment(String username, String comment)
	{
		comments.addComment(username, comment);
		reward_state.addComment(username);
	}
	
	public synchronized void addRewin(Integer postId)
	{
		rewins.add(postId);
	}
	
	public synchronized void removeRewin(Integer postId)
	{
		rewins.remove(postId);
	}
	
	public synchronized Reward calculateReward()
	{
		return reward_state.calcLastReward();
	}
	
	@JsonIgnore() public synchronized Integer getPositiveRatings()
	{
		return likes.getLikes();
	}
	
	@JsonIgnore() public synchronized Integer getNegativeRatings()
	{
		return likes.getDislikes();
	}
	
	@JsonIgnore() public synchronized List<Comment> getComments()
	{
		return comments.getComments();
	}
}

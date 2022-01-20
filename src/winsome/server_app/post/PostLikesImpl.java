package winsome.server_app.post;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.server_app.post.exceptions.CannotRateException;

@JsonTypeName("post_likes_impl")
public class PostLikesImpl implements PostLikes
{
	@JsonProperty() private int positive_likes;
	@JsonProperty() private int negative_likes;
	@JsonProperty() private Set<String> who_liked;
	
	public PostLikesImpl()
	{
		positive_likes = 0;
		negative_likes = 0;
		who_liked = new HashSet<String>();
	}
	
	public PostLikesImpl(int positive_likes, int negative_likes, Set<String> who_liked)
	{
		this.positive_likes = positive_likes;
		this.negative_likes = negative_likes;
		this.who_liked = new HashSet<String>(who_liked);
	}

	@Override
	public void addLike(String username)
	{
		checkCanRate(username);
		positive_likes++;
		who_liked.add(username);
	}

	@Override
	public void addDislike(String username)
	{
		checkCanRate(username);
		negative_likes++;
		who_liked.add(username);
	}
	
	private void checkCanRate(String username)
	{
		if(!canRate(username))
		{
			throw new CannotRateException();
		}
	}

	@Override
	@JsonIgnore
	public int getLikes()
	{
		return positive_likes;
	}

	@Override
	@JsonIgnore
	public int getDislikes()
	{
		return negative_likes;
	}

	@Override
	public boolean canRate(String username)
	{
		return !who_liked.contains(username);
	}
	
	@Override
	public PostLikesImpl clone()
	{
		return new PostLikesImpl(positive_likes, negative_likes, who_liked);
	}
}

package winsome.server.post;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public interface PostLikes extends Cloneable
{
	void addLike(String username);
	void addDislike(String username);
	int getLikes();
	int getDislikes();
	boolean canRate(String username);
	
	PostLikes clone();
}

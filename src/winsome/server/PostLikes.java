package winsome.server;

public interface PostLikes
{
	void addLike(String username);
	void addDislike(String username);
	boolean canRate(String username);
}

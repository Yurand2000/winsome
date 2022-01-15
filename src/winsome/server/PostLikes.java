package winsome.server;

public interface PostLikes
{
	void addLike(String username);
	void addDislike(String username);
	int getLikes();
	int getDislikes();
	boolean canRate(String username);
}

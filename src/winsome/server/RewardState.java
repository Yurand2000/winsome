package winsome.server;

public interface RewardState
{
	void addLike();
	void addDislike();
	void addComment(String username);
	Integer calcLastReward();
}

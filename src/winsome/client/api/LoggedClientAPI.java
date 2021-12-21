package winsome.client.api;

import java.util.List;

public interface LoggedClientAPI
{
	List<User> listUsers();
	List<User> listFollowers();
	List<User> listFollowing();
	void followUser(String username);
	void unfollowUser(String username);
	List<PostShort> viewBlog();
	Integer createPost(String title, String content);
	List<PostShort> showFeed();
	Post showPost(Integer postId);
	void deletePost(Integer postId);
	Integer rewinPost(Integer postId);
	void ratePost(Integer postId, boolean rating);
	void addComment(Integer postId, String comment);
	Wallet getWallet();
	Integer getWalletInBitcoin();
}

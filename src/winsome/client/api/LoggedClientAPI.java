package winsome.client.api;

import java.util.List;

public interface LoggedClientAPI
{
	void logout();
	List<User> listUsers();
	List<User> listFollowers();
	List<User> listFollowing();
	void followUser(String username);
	void unfollowUser(String username);
	List<PostShort> viewBlog();
	void createPost(String title, String content);
	List<PostShort> showFeed();
	Post showPost(Integer postId);
	void deletePost(Integer postId);
	void rewinPost(Integer postId);
	void ratePost(Integer postId, Post.Rating rating);
}

package winsome.client_app.api;

import java.util.List;

public interface LoggedClientAPI
{
	/**
	 * Lists the users that have at least one tag in common with the logged user.
	 */
	List<User> listUsers();
	
	/**
	 * Lists the users that are now following the logged user.
	 * This list is retrived locally and not queried to the server.
	 */
	List<User> listFollowers();
	
	/**
	 * Lists the users that the logged user is following.
	 * This list is retrived locally and not queried to the server.
	 */
	List<User> listFollowing();
	
	/**
	 * Requests to follow the given user.
	 * @param username
	 */
	void followUser(String username);
	
	/**
	 * Requests to unfollow the given user.
	 * @param username
	 */
	void unfollowUser(String username);
	
	/**
	 * Requests the list of posts created by the logged user.
	 */
	List<PostShort> viewBlog();
	
	/**
	 * Requests to create a post for the current user.
	 * @param title The title of the post, must be at most 20 characters long.
	 * @param content The content of the post, must be at most 500 characters long.
	 * @return The unique identifier of the newly created post.
	 */
	Integer createPost(String title, String content);
	
	/**
	 * Requests the list of posts created by the users the logged user is following.
	 */
	List<PostShort> showFeed();
	
	/**
	 * Retrives the post by the given id.
	 * @param postId
	 * @return The post identified by the given id.
	 */
	Post showPost(Integer postId);
	
	/**
	 * Deletes the post identified by the given id, if possible.
	 * You can delete a post only if you are the author.
	 * @param postId
	 */
	void deletePost(Integer postId);
	
	/**
	 * Rewins a post identified by the given id, if possible.
	 * You can rewin a post only if you are not the author of it and it belongs to your feed.
	 * @param postId
	 * @return The unique identifier of the newly created post.
	 */
	Integer rewinPost(Integer postId);
	
	/**
	 * Rate a post by the given post id.
	 * @param postId The post to rate.
	 * @param rating The rating: true if positive, false if negative.
	 */
	void ratePost(Integer postId, boolean rating);
	
	/**
	 * Adds a comment to the given post.
	 * @param postId The post to comment.
	 * @param comment The comment.
	 */
	void addComment(Integer postId, String comment);
	
	/**
	 * Gets the wallet of the current user.
	 */
	Wallet getWallet();
	
	/**
	 * Gets the value of the logged user wallet in bitcoins.
	 */
	Integer getWalletInBitcoin();
}

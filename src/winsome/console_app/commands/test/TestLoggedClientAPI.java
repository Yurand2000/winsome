package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import winsome.client_app.api.LoggedClientAPI;
import winsome.client_app.api.Post;
import winsome.client_app.api.PostShort;
import winsome.client_app.api.User;
import winsome.client_app.api.Wallet;

class TestLoggedClientAPI implements LoggedClientAPI
{

	private boolean list_users_called = false;
	
	@Override
	public List<User> listUsers()
	{
		list_users_called = true;
		return Arrays.asList(new User[] { new User("User") });
	}
	
	public void checkListUsersExecuted()
	{
		assertTrue(list_users_called);
		list_users_called = false;
	}
	
	

	private boolean list_follower_called = false;
	
	@Override
	public List<User> listFollowers()
	{
		list_follower_called = true;
		return Arrays.asList(new User[] { new User("User") });
	}
	
	public void checkListFollowersExecuted()
	{
		assertTrue(list_follower_called);
		list_follower_called = false;
	}

	
	
	private boolean list_following_called = false;
	
	@Override
	public List<User> listFollowing()
	{
		list_following_called = true;
		return Arrays.asList(new User[] { new User("User") });
	}
	
	public void checkListFollowingExecuted()
	{
		assertTrue(list_following_called);
		list_following_called = false;
	}
	
	

	private String follow_user = null;
	private boolean follow_user_called = false;
	
	@Override
	public void followUser(String username)
	{
		follow_user_called = true;
		assertEquals(follow_user, username);
	}
	
	public void setFollowUserExpected(String username)
	{
		follow_user = username;
	}
	
	public void checkFollowUserExecuted()
	{
		assertTrue(follow_user_called);
		follow_user_called = false;
	}
	
	
	private String unfollow_user = null;
	private boolean unfollow_user_called = false;

	@Override
	public void unfollowUser(String username)
	{
		unfollow_user_called = true;
		assertEquals(unfollow_user, username);
	}
	
	public void setUnfollowUserExpected(String username)
	{
		unfollow_user = username;
	}
	
	public void checkUnfollowUserExecuted()
	{
		assertTrue(unfollow_user_called);
		unfollow_user_called = false;
	}
	
	
	
	
	private boolean view_blog_called = false;

	@Override
	public List<PostShort> viewBlog()
	{
		view_blog_called = true;
		return Arrays.asList(new PostShort[] { new PostShort(50, "Author", "Title") });
	}
	
	public void checkViewBlogExecuted()
	{
		assertTrue(view_blog_called);
		view_blog_called = false;
	}
	
	
	

	private String create_post_title = null;
	private String create_post_content = null;
	private boolean create_post_executed = false;
	
	@Override
	public Integer createPost(String title, String content)
	{
		create_post_executed = true;
		assertEquals(create_post_title, title);
		assertEquals(create_post_content, content);
		return 5;
	}
	
	public void setCreatePostExpected(String title, String content)
	{
		create_post_title = title;
		create_post_content = content;
	}
	
	public void checkCreatePostExecuted()
	{
		assertTrue(create_post_executed);
		create_post_executed = false;
	}
	
	
	
	private boolean show_feed_called = false;

	@Override
	public List<PostShort> showFeed()
	{
		show_feed_called = true;
		return Arrays.asList(new PostShort[] { new PostShort(50, "Author", "Title") });
	}
	
	public void checkShowFeedExecuted()
	{
		assertTrue(show_feed_called);
		show_feed_called = false;
	}
	
	

	private Integer show_postId = null;
	private boolean show_post_executed = false;

	@Override
	public Post showPost(Integer postId)
	{
		show_post_executed = true;
		assertEquals(postId, show_postId);
		return new Post(postId, "", "", "", 0, 0, Arrays.asList(new Post.Comment[] { new Post.Comment("user", "ciao") }));
	}
	
	public void setShowPostExpected(Integer postId)
	{
		show_postId = postId;
	}
	
	public void checkShowPostExecuted()
	{
		assertTrue(show_post_executed);
		show_post_executed = false;
	}
	
	
	
	private Integer delete_postId = null;
	private boolean delete_executed = false;

	@Override
	public void deletePost(Integer postId)
	{
		delete_executed = true;
		assertEquals(postId, delete_postId);
	}
	
	public void setDeletePostExpected(Integer postId)
	{
		delete_postId = postId;
	}
	
	public void checkDeletePostExecuted()
	{
		assertTrue(delete_executed);
		delete_executed = false;
	}
	
	
	
	private Integer rewin_postId = null;
	private boolean rewin_post_executed = false;

	@Override
	public Integer rewinPost(Integer postId)
	{
		rewin_post_executed = true;
		assertEquals(postId, rewin_postId);
		return postId + 1;
	}
	
	public void setRewinPostExpected(Integer postId)
	{
		rewin_postId = postId;
	}
	
	public void checkRewinPostExecuted()
	{
		assertTrue(rewin_post_executed);
		rewin_post_executed = false;
	}
	
	
	
	private Integer rate_post_id = null;
	private Boolean rate_post_rating = null;
	private boolean rate_post_executed = false;

	@Override
	public void ratePost(Integer postId, boolean rating)
	{
		rate_post_executed = true;
		assertEquals(postId, rate_post_id);
		assertEquals(rate_post_rating, rating);
	}
	
	public void setRatePostExpected(Integer postId, boolean rating)
	{
		rate_post_id = postId;
		rate_post_rating = rating;
	}
	
	public void checkRatePostExecuted()
	{
		assertTrue(rate_post_executed);
		rate_post_executed = false;
	}
	
	
	

	private Integer comment_postId = null;
	private String comment_string = null;
	private boolean add_comment_executed = false;
	
	@Override
	public void addComment(Integer postId, String comment)
	{
		assertEquals(postId, comment_postId);
		assertEquals(comment, comment_string);
		add_comment_executed = true;
	}
	
	public void setAddCommentExpected(Integer postId, String comment)
	{
		comment_postId = postId;
		comment_string = comment;
	}
	
	public void checkAddCommentExecuted()
	{
		assertTrue(add_comment_executed);
		add_comment_executed = false;
	}
	
	

	private boolean get_wallet_called = false;

	@Override
	public Wallet getWallet()
	{
		get_wallet_called = true;
		return new Wallet(0.0, new ArrayList<Wallet.Transaction>());
	}
	
	public void checkGetWalletExecuted()
	{
		assertTrue(get_wallet_called);
		get_wallet_called = false;
	}
	
	

	private boolean get_wallet_in_bitcoin_called = false;

	@Override
	public Integer getWalletInBitcoin()
	{
		get_wallet_in_bitcoin_called = true;
		return 50;
	}
	
	public void checkGetWalletInBitcoinExecuted()
	{
		assertTrue(get_wallet_in_bitcoin_called);
		get_wallet_in_bitcoin_called = false;
	}

}

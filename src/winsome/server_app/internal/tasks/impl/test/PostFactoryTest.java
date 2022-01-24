package winsome.server_app.internal.tasks.impl.test;

import java.util.HashSet;
import java.util.Set;

import winsome.server_app.post.Content;
import winsome.server_app.post.ContentPost;
import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostCommentsImpl;
import winsome.server_app.post.PostFactoryImpl;
import winsome.server_app.post.PostLikesImpl;
import winsome.server_app.post.RewardStateImpl;
import winsome.server_app.post.RewinPost;

class PostFactoryTest extends PostFactoryImpl
{
	public boolean makeNewPost_called = false;

	@Override
	public GenericPost makeNewPost(String title, String author, String content)
	{
		makeNewPost_called = true;
		return super.makeNewPost(title, author, content);
	}
	
	public GenericPost makeNewPostId(Integer newPostId, String title, String author, String content)
	{
		Content data = new Content(title, author, content);
	
		return new ContentPost(newPostId, data, new HashSet<Integer>(),
			new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
	}

	public boolean makeRewinPost_called = false; 
	
	@Override
	public GenericPost makeRewinPost(Integer postId, String author)
	{
		makeRewinPost_called = true;

		return new RewinPost(postId + 1, postId, author, new HashSet<Integer>(),
			new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
		
		//return super.makeRewinPost(postId, author);
	}
	
	public GenericPost makeRewinPostId(Integer newPostId, Integer postId, String author)
	{
		return new RewinPost(newPostId, postId, author, new HashSet<Integer>(),
			new PostLikesImpl(), new PostCommentsImpl(), new RewardStateImpl());
	}
	
	public boolean signalPostDelete_called = false;
	public final Set<Integer> signalledPosts = new HashSet<Integer>();

	@Override
	public void signalPostDeleted(Integer postId)
	{
		signalledPosts.add(postId);
		signalPostDelete_called = true;
	}

}

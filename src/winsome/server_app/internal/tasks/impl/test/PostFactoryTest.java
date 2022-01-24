package winsome.server_app.internal.tasks.impl.test;

import static org.junit.jupiter.api.Assertions.fail;

import winsome.server_app.post.GenericPost;
import winsome.server_app.post.PostFactoryImpl;

class PostFactoryTest extends PostFactoryImpl
{
	public boolean makeNewPost_called = false; 

	@Override
	public GenericPost makeNewPost(String title, String author, String content)
	{
		makeNewPost_called = true;
		return super.makeNewPost(title, author, content);
	}

	public boolean makeRewinPost_called = false; 
	
	@Override
	public GenericPost makeRewinPost(Integer postId, String author)
	{
		makeRewinPost_called = true;
		return super.makeRewinPost(postId, author);
	}

	@Override
	public void signalPostDeleted(Integer postId)
	{
		fail();
	}

}

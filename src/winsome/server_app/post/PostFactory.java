package winsome.server_app.post;

public interface PostFactory
{	
	GenericPost makeNewPost(String title, String author, String content);	
	GenericPost makeRewinPost(Integer postId, String author);
	void signalPostDeleted(Integer postId);
}

package winsome.connection.socket_messages.server;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.client_app.api.PostShort;
import winsome.connection.socket_messages.Message;

@JsonTypeName("get_blog_answer")
public class GetBlogAnswer extends Message
{
	public final PostShort[] blog;
	
	@SuppressWarnings("unused")
	private GetBlogAnswer() { blog = null; }
	
	public GetBlogAnswer(List<PostShort> blog)
	{
		this.blog = blog.toArray(new PostShort[0]);
	}
	
	public GetBlogAnswer(PostShort[] blog)
	{
		this.blog = blog;
	}
}

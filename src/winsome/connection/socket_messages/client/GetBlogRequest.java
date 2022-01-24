package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("get_blog_request")
@JsonSerialize
public class GetBlogRequest extends Message
{
	public GetBlogRequest() { }
}

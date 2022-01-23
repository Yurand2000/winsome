package winsome.connection.socket_messages.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import winsome.connection.socket_messages.Message;

@JsonTypeName("get_feed_request")
@JsonSerialize
public class GetFeedRequest extends Message
{
	public GetFeedRequest() { }
}

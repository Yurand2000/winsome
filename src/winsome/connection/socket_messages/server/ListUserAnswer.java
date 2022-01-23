package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("list_user_answer")
public class ListUserAnswer extends Message
{
	public final String[] similar_users;
	
	@SuppressWarnings("unused")
	private ListUserAnswer() { similar_users = null; }
	
	public ListUserAnswer(String[] similar_users)
	{
		this.similar_users = similar_users;
	}
}

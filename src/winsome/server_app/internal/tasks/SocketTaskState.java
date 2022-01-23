package winsome.server_app.internal.tasks;

import winsome.connection.socket_messages.Message;

public interface SocketTaskState
{
	String getClientUser();
	void setClientUser(String username);
	
	Message getRequestMessage();
	void sendAnswerMessage(Message answer); 
}

package winsome.connection.server_api.socket;

import winsome.connection.socket_messages.Message;
import winsome.server_app.internal.tasks.SocketTaskState;

public interface SocketState extends SocketTaskState
{
	SocketReader getReader();
	SocketWriter getWriter();
	void setRequestMessage(Message incoming);
}

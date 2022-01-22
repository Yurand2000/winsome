package winsome.connection.client_api.socket;

import java.io.IOException;

import winsome.client_app.api.exceptions.ServerInternalException;
import winsome.connection.socket_messages.Message;
import winsome.connection.socket_messages.server.RequestExceptionAnswer;

public interface ConnectionHandler
{
	void connect() throws IOException;
	<T extends Message> T readMessage(Class<T> type) throws IOException, ServerInternalException, RequestExceptionAnswer.Exception;
	<T extends Message> void sendMessage(T message) throws IOException;
	void disconnect();
}

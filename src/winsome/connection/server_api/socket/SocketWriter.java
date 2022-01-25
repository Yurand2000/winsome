package winsome.connection.server_api.socket;

import java.io.IOException;

public interface SocketWriter
{
	void executeWriteOperation() throws IOException;
	void addMessageToSend(byte[] data);
}

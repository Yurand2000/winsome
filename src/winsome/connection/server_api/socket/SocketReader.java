package winsome.connection.server_api.socket;

import java.io.IOException;

public interface SocketReader
{
	void executeReadOperation() throws IOException;
	boolean hasMessageBeenRetrived();
	byte[] getRetrivedMessage();
}

package winsome.connection.server_api.socket;

public interface SocketReader
{
	void executeReadOperation();
	boolean hasMessageBeenRetrived();
	byte[] getRetrivedMessage();
}

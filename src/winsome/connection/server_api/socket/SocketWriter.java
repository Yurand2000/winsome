package winsome.connection.server_api.socket;

public interface SocketWriter
{
	void executeWriteOperation();
	void addMessageToSend(byte[] data);
}

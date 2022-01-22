package winsome.connection.client_api.socket.test.connectionHandlerImpl;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("test")
public class MessageTest extends Message
{
	public String message;
	
	public MessageTest() { message = "ciao"; }
	
	public MessageTest(String msg) { message = msg; }
}

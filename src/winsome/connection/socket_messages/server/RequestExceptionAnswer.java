package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.client_app.api.APIException;
import winsome.connection.socket_messages.Message;

@JsonTypeName("exception")
public class RequestExceptionAnswer extends Message
{
	public final String message;
	
	@SuppressWarnings("unused")
	private RequestExceptionAnswer() { message = null; }
	
	public RequestExceptionAnswer(String message)
	{
		this.message = message;
	}
	
	public Exception makeException()
	{
		return new Exception(message);
	}
	
	public static class Exception extends APIException
	{
		private static final long serialVersionUID = 1L;
		
		public final String message;
		
		public Exception(String message)
		{
			super("Server answered with exception message: " + message);
			this.message = message;
		}
	}
}

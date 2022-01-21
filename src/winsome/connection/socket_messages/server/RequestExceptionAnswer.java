package winsome.connection.socket_messages.server;

import com.fasterxml.jackson.annotation.JsonTypeName;

import winsome.connection.socket_messages.Message;

@JsonTypeName("exception")
public class RequestExceptionAnswer extends Message
{
	public final String message;
	
	public RequestExceptionAnswer(String message)
	{
		this.message = message;
	}
	
	public RequestException makeException()
	{
		return new RequestException(message);
	}
	
	public static class RequestException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		
		public final String message;
		
		public RequestException(String message)
		{
			super("Server answered with exception message: " + message);
			this.message = message;
		}
	}
}

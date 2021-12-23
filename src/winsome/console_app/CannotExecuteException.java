package winsome.console_app;

public class CannotExecuteException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public CannotExecuteException(String error)
	{
		super("Cannot execute: " + error);
	}
}

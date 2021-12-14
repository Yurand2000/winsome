package winsome.console;

public class CannotExecuteException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public CannotExecuteException()
	{
		super("Cannot execute: Unknown command.");
	}
}

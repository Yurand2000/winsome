package winsome.console_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import winsome.client_app.api.APIException;

public class ConsoleExecutorRunnable implements Runnable
{
	private ConsoleCommandExecutor chain;
	private InputStream input;
	private PrintStream output;
	
	public ConsoleExecutorRunnable()
	{
		chain = new ConsoleCommandExecutor(null);
		input = System.in;
		output = System.out;
	}
	
	public synchronized void setExecutorChain(ConsoleCommandExecutor executor_chain)
	{
		chain = executor_chain;
	}

	@Override
	public void run()
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while(!Thread.currentThread().isInterrupted())
		{
			try
			{
				String line = reader.readLine();
				
				if(!Thread.currentThread().isInterrupted() && !line.isEmpty())
				{
					executeCommand(line);
				}
			}
			catch (IOException e) { }
		}
	}
	
	private void executeCommand(String line)
	{
		try
		{
			printLine(execute(line));
		}
		catch(APIException | CannotExecuteException e)
		{
			printLine(e.getMessage());
		}
	}
	
	private synchronized String execute(String line)
	{
		return chain.executeString(line);
	}
	
	private void printLine(String line)
	{
		synchronized(output)
		{
			output.println(line);
		}
	}
}

package winsome.console;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleExecutorClass
{	
	private Thread consoleExecutorThread;
	private ConsoleExecutorRunnable consoleExecutorRunnable;
	
	public ConsoleExecutorClass()
	{
		consoleExecutorThread = null;
		consoleExecutorRunnable = new ConsoleExecutorRunnable();
	}
	
	public void startConsoleExecutor()
	{
		if(consoleExecutorThread == null)
		{
			consoleExecutorThread = new Thread(consoleExecutorRunnable);
			consoleExecutorThread.setName("console-executor");
			consoleExecutorThread.setDaemon(true);
			consoleExecutorThread.start();
		}
		else
		{
			throw new IllegalStateException("consoleExecutor already running.");
		}
	}
	
	public void joinConsoleExecutor() throws InterruptedException
	{
		if(consoleExecutorThread != null)
		{
			consoleExecutorThread.join();
			consoleExecutorThread = null;
		}
	}
	
	public void setExecutorChain(ConsoleCommandExecutor executor_chain)
	{		
		consoleExecutorRunnable.setExecutorChain(executor_chain);
	}
	
	private class ConsoleExecutorRunnable implements Runnable
	{
		private ConsoleCommandExecutor chain;
		
		public ConsoleExecutorRunnable()
		{
			chain = new ConsoleCommandExecutor(null);
		}
		
		public synchronized void setExecutorChain(ConsoleCommandExecutor executor_chain)
		{
			chain = executor_chain;
		}

		@Override
		public void run()
		{
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				while(!Thread.currentThread().isInterrupted())
				{
					String line = reader.readLine();
					
					if(!Thread.currentThread().isInterrupted())
					{
						String output = execute(line);						
						System.out.println(output);
					}
				}
			}
			catch(CannotExecuteException e)
			{
				System.out.println(e.getMessage());
			}
			catch (IOException e) { }
		}
		
		private synchronized String execute(String line)
		{
			return chain.executeString(line);
		}
	}
}

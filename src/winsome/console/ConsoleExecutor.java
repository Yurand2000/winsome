package winsome.console;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleExecutor
{	
	private static ConsoleExecutor instance = null;
	
	private Thread consoleExecutorThread;
	private ConsoleExecutorRunnable consoleExecutorRunnable;
	
	public static ConsoleExecutor instance()
	{
		if(instance == null)
		{
			instance = new ConsoleExecutor();
		}
		return instance;
	}
	
	private ConsoleExecutor()
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
	
	public void setExecutorChain(List<Class<? extends ConsoleCommandExecutor>> executors)
	{
		try
		{
			ConsoleCommandExecutor root = null;
			
			for(Class<? extends ConsoleCommandExecutor> executor_class : executors)
			{
				root = executor_class.getConstructor(ConsoleCommandExecutor.class).newInstance(root);
			}
			
			setExecutorChain(root);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			System.err.println("Couldn't create the Command Executor chain.");
			e.printStackTrace();
		}
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

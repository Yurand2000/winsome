package winsome.console_app;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConsoleExecutor
{	
	private static ConsoleExecutor instance = null;
	
	private Thread consoleExecutorThread;
	private ConsoleExecutorRunnable consoleExecutorRunnable;
	
	private static ConsoleExecutor instance()
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
	
	public static void startConsoleExecutor()
	{
		instance().startConsoleExecutorPrivate();
	}
	
	private void startConsoleExecutorPrivate()
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
	
	public static void joinConsoleExecutor() throws InterruptedException
	{
		instance().joinConsoleExecutorPrivate();
	}
	
	private void joinConsoleExecutorPrivate() throws InterruptedException
	{
		if(consoleExecutorThread != null)
		{
			consoleExecutorThread.interrupt();
			consoleExecutorThread.join();
			consoleExecutorThread = null;
		}
	}
	
	public static void setExecutorChain(List<Class<? extends ConsoleCommandExecutor>> executors)
	{
		instance().setExecutorChainPrivate(executors);
	}
	
	private void setExecutorChainPrivate(List<Class<? extends ConsoleCommandExecutor>> executors)
	{
		try
		{
			ConsoleCommandExecutor root = null;
			
			for(Class<? extends ConsoleCommandExecutor> executor_class : executors)
			{
				root = executor_class.getConstructor(ConsoleCommandExecutor.class).newInstance(root);
			}
			
			setExecutorChainPrivate(root);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			System.err.println("Couldn't create the Command Executor chain.");
			e.printStackTrace();
		}
	}
	
	private void setExecutorChainPrivate(ConsoleCommandExecutor executor_chain)
	{		
		consoleExecutorRunnable.setExecutorChain(executor_chain);
	}
}

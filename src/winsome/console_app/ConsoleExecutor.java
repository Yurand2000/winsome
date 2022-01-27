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
		instance().__startConsoleExecutor();
	}
	
	private void __startConsoleExecutor()
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
		instance().__joinConsoleExecutor();
	}
	
	private void __joinConsoleExecutor() throws InterruptedException
	{
		if(consoleExecutorThread != null)
		{
			consoleExecutorThread.join();
			consoleExecutorThread = null;
		}
	}
	
	public static void setExecutorChain(List<Class<? extends ConsoleCommandExecutor>> executors)
	{
		instance().__setExecutorChain(executors);
	}
	
	private void __setExecutorChain(List<Class<? extends ConsoleCommandExecutor>> executors)
	{
		try
		{
			ConsoleCommandExecutor chain = createExecutorChain(executors);
			consoleExecutorRunnable.setExecutorChain(chain);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			System.err.println("Couldn't create the Command Executor chain.");
			e.printStackTrace();
		}
	}
	
	private ConsoleCommandExecutor createExecutorChain(List<Class<? extends ConsoleCommandExecutor>> executors)
		throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		ConsoleCommandExecutor root = null;
		
		for(Class<? extends ConsoleCommandExecutor> executor_class : executors)
		{
			root = executor_class.getConstructor(ConsoleCommandExecutor.class).newInstance(root);
		}
		
		return root;
	}
}

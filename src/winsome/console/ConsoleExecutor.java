package winsome.console;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConsoleExecutor
{
	private static ConsoleExecutorClass instance = null;
	
	public static void startConsoleExecutor()
	{
		instantiate();
		instance.startConsoleExecutor();
	}
	
	public static void joinConsoleExecutor()
	{
		instantiate();
		instance.joinConsoleExecutor();
	}
	
	public static void setCommandExecutors(List<Class<? extends ConsoleCommandExecutor>> executors)
	{
		try
		{
			ConsoleCommandExecutor root = null;
			
			for(Class<? extends ConsoleCommandExecutor> executor_class : executors)
			{
				root = executor_class.getConstructor(ConsoleCommandExecutor.class).newInstance(root);
			}
			
			instance.setExecutorChain(root);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			System.err.println("Couldn't create the Command Executor chain.");
			e.printStackTrace();
		}
	}
	
	private static void instantiate()
	{
		if(instance == null)
		{
			instance = new ConsoleExecutorClass();
		}
	}
	
	private ConsoleExecutor() { }
}

package winsome.console;

import java.util.List;
import java.util.ArrayList;

public class ConsoleAppMain
{
	public static void main(String[] args) throws InterruptedException
	{
		ConsoleExecutor.startConsoleExecutor();
		
		List<Class<? extends ConsoleCommandExecutor>> chain_executors = new ArrayList<Class<? extends ConsoleCommandExecutor>>();
		chain_executors.add(AnyCommandExecutor.class);
		chain_executors.add(ExitCommandExecutor.class);
		
		ConsoleExecutor.setCommandExecutors(chain_executors);
		
		System.out.println("Already");
		while(!Thread.currentThread().isInterrupted())
		{
			
		}
		
		ConsoleExecutor.stopConsoleExecutor();
	}
}

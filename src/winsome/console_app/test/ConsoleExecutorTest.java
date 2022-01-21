package winsome.console_app.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.test.consoleExecutor.ConsoleExecutorRunnableTest;
import winsome.console_app.ConsoleCommandExecutor;
import winsome.console_app.ConsoleExecutor;

class ConsoleExecutorTest
{
	private ConsoleExecutorRunnableTest runnable;
	
	@BeforeEach
	void setup() throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		runnable = new ConsoleExecutorRunnableTest();
		
		Method method = ConsoleExecutor.class.getDeclaredMethod("instance");
		method.setAccessible(true);
		method.invoke(null);

		Field instance = ConsoleExecutor.class.getDeclaredField("instance");
		instance.setAccessible(true);
		Field runnable = ConsoleExecutor.class.getDeclaredField("consoleExecutorRunnable");
		runnable.setAccessible(true);
		runnable.set(instance.get(null), this.runnable);
	}

	@Test
	void testSetExecutorChain()
	{
		List<Class<? extends ConsoleCommandExecutor>> chain = new ArrayList<Class<? extends ConsoleCommandExecutor>>();
		chain.add(ConsoleCommandExecutor.class);

		runnable.setExpectedExecutors(ConsoleCommandExecutor.class);
		ConsoleExecutor.setExecutorChain(chain);
		runnable.checkSetExecutorCalled();
	}
	
	@Test
	void testStartConsoleExecutor() throws InterruptedException
	{
		List<Class<? extends ConsoleCommandExecutor>> chain = new ArrayList<Class<? extends ConsoleCommandExecutor>>();
		chain.add(ConsoleCommandExecutor.class);

		runnable.setExpectedExecutors(ConsoleCommandExecutor.class);
		ConsoleExecutor.setExecutorChain(chain);
		
		ConsoleExecutor.startConsoleExecutor();
		ConsoleExecutor.joinConsoleExecutor();
		
		runnable.checkRunCalled();		
	}

}

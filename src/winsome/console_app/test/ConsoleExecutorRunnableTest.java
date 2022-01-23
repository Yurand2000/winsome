package winsome.console_app.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.console_app.ConsoleCommandExecutor;
import winsome.console_app.ConsoleExecutorRunnable;

class ConsoleExecutorRunnableTest extends ConsoleExecutorRunnable
{
	private boolean setExecutorCalled = false;
	private boolean run_called = false;
	private Class<? extends ConsoleCommandExecutor> expectedExecutor = null;
	
	public ConsoleExecutorRunnableTest()
	{
		super();
	}

	@Override
	public synchronized void setExecutorChain(ConsoleCommandExecutor executor_chain)
	{
		setExecutorCalled = true;
		assertEquals(expectedExecutor, executor_chain.getClass());
	}
	
	public void setExpectedExecutors(Class<? extends ConsoleCommandExecutor> expected)
	{
		expectedExecutor = expected;
	}
	
	public void checkSetExecutorCalled()
	{
		assertTrue(setExecutorCalled);
		setExecutorCalled = false;
	}
	
	@Override
	public void run()
	{
		run_called = true;
	}
	
	public void checkRunCalled()
	{
		assertTrue(run_called);
		run_called = false;
	}
}

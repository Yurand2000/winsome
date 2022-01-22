package winsome.server_app.internal.pausable_threads.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import winsome.server_app.internal.pausable_threads.PausableThread;
import winsome.server_app.internal.pausable_threads.PausableThreadMonitor;

class PausableThreadTest
{
	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testPausableThread() throws InterruptedException
	{
		AtomicBoolean executed = new AtomicBoolean(false);
		PausableThreadMonitor monitor = new PausableThreadMonitor();
		PausableThread thread = new PausableThread(monitor, () -> {
			executed.set(true);
		});
		
		monitor.pauseAllThreads();
		
		thread.start();
		Thread.yield();
		Thread.sleep(100);
		
		monitor.resumeAllThreads();
		
		thread.join();

		assertTrue(!thread.isInterrupted());
		assertTrue(!Thread.currentThread().isInterrupted());
		assertTrue(executed.get());		
	}

}

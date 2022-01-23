package winsome.server_app.internal.pausable_threads.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import winsome.server_app.internal.pausable_threads.PausableThreadMonitor;

class TEST_PausableThreadMonitor
{
	private PausableThreadMonitor monitor;

	@BeforeEach
	void setup()
	{
		monitor = new PausableThreadMonitor();
	}
	
	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testConstructor() throws InterruptedException
	{		
		monitor.checkNotPaused();
		
		assertFalse(Thread.currentThread().isInterrupted());
	}

	@Test
	@Timeout(value= 500, unit= TimeUnit.MILLISECONDS)
	void testPauseAndResumeAllThreads() throws InterruptedException
	{		
		monitor = new PausableThreadMonitor();
		monitor.pauseAllThreads();
		
		AtomicBoolean executed = new AtomicBoolean(false);
		Thread thread = new Thread(() -> {			
			try
			{ monitor.checkNotPaused(); }
			catch (InterruptedException e) { }
			
			executed.set(true);			
		});
		thread.start();
		
		monitor.resumeAllThreads();
		
		thread.join();

		assertTrue(!thread.isInterrupted());
		assertTrue(!Thread.currentThread().isInterrupted());
		assertTrue(executed.get());
	}
}

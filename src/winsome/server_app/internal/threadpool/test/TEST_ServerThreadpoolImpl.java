package winsome.server_app.internal.threadpool.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolImpl;

class TEST_ServerThreadpoolImpl
{
	ServerThreadpoolImpl pool;

	@BeforeEach
	void setup()
	{
		pool = new ServerThreadpoolImpl();
		pool.startThreadpool();
	}
	
	@Test
	void testTaskExecuted() throws InterruptedException
	{
		AtomicBoolean executed = new AtomicBoolean(false);
		
		pool.enqueueTask( (ServerThreadpool pool) ->
		{
			synchronized(this)
			{
				executed.set(true);
				notifyAll();
			}
		} );
		
		synchronized(this)
		{
			while(!executed.get()) wait();
		}
		
		assertTrue(executed.get());
	}
	
	@Test
	void testTaskCanBePaused() throws InterruptedException
	{
		AtomicBoolean executed = new AtomicBoolean(false);
		
		pool.pauseThreadpool();
		
		pool.enqueueTask( (ServerThreadpool pool) ->
		{
			synchronized(this)
			{
				executed.set(true);
				notifyAll();
			}
		} );

		Thread.yield();
		Thread.sleep(200);
		assertFalse(executed.get());
		
		pool.resumeThreadpool();
		
		synchronized(this)
		{
			while(!executed.get()) wait();
		}
		
		assertTrue(executed.get());
	}

	@AfterEach
	void teardown()
	{
		pool.stopThreadpool();
	}
}

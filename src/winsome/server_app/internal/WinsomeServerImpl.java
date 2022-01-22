package winsome.server_app.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import winsome.generic.SerializerWrapper;
import winsome.server_app.internal.pausable_threads.PausableThreadFactory;
import winsome.server_app.internal.tasks.WinsomeRunnable;
import winsome.server_app.internal.tasks.WinsomeTask;

public class WinsomeServerImpl implements WinsomeServer
{
	public final ServerSettings settings;
	private final WinsomeData server_data;
	private ExecutorService thread_pool;
	private final PausableThreadFactory thread_factory;
	
	public WinsomeServerImpl(ServerSettings settings)
	{
		this.settings = settings;
		thread_factory = new PausableThreadFactory();
		thread_pool = null;
		server_data = tryGetSerializedData(settings.save_file);		
	}
	
	private WinsomeData tryGetSerializedData(String filename)
	{
		if(filename == null || !Files.exists(Paths.get(filename)))
		{
			return new WinsomeData();
		}
		else
		{
			try 
			{
				byte[] data = Files.readAllBytes(Paths.get(filename));
				return SerializerWrapper.deserialize(data, WinsomeData.class);				
			}
			catch (IOException e)
			{
				System.err.println(e.getMessage());
				return new WinsomeData();
			}
		}
	}
	
	public void startServer()
	{
		int processors = Runtime.getRuntime().availableProcessors();
		thread_pool = Executors.newFixedThreadPool(processors, thread_factory);
	}
	
	public void shutdownServer()
	{
		thread_pool.shutdown();
		try { thread_pool.awaitTermination(2, TimeUnit.MINUTES); }
		catch (InterruptedException e) { e.printStackTrace(); }
		thread_pool.shutdownNow();
		saveToFile();
	}
	
	public void saveToFile()
	{
		pauseTaskExecutors();		
		WinsomeData clone = server_data.clone();		
		resumeTaskExecutors();
		serializeAndSave(clone);
	}
	
	private void pauseTaskExecutors()
	{
		thread_factory.pauseAllExecutions();
	}
	
	private void resumeTaskExecutors()
	{
		thread_factory.resumeAllExecutions();
	}
	
	private void serializeAndSave(Object obj)
	{
		try
		{
			byte[] data = SerializerWrapper.serialize(obj);
			Files.write(Paths.get(settings.save_file), data);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public void executeTask(WinsomeTask task)
	{
		WinsomeRunnable runnable = new WinsomeRunnable(this, server_data, task);
		thread_pool.execute(runnable);
	}
	
	public void executeTaskNow(WinsomeTask task)
	{
		WinsomeRunnable runnable = new WinsomeRunnable(this, server_data, task);
		runnable.run();
	}
}

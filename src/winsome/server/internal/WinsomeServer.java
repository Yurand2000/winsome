package winsome.server.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import winsome.generic.SerializerWrapper;

public class WinsomeServer
{
	private final String save_file;
	private final WinsomeData server_data;
	private final ExecutorService thread_pool;
	private final PausableThreadFactory thread_factory;
	
	public WinsomeServer(String filename)
	{
		save_file = filename;
		thread_factory = new PausableThreadFactory();
		thread_pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), thread_factory);
		server_data = tryGetSerializedData(save_file);		
	}
	
	private WinsomeData tryGetSerializedData(String filename)
	{
		if(filename == null)
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
	
	public void shutdownServer() throws InterruptedException
	{
		thread_pool.shutdown();
		thread_pool.awaitTermination(2, TimeUnit.MINUTES);
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
			Files.write(Paths.get(save_file), data);
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
}

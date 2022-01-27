package winsome.server_app.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import winsome.connection.server_api.follower_updater.FollowerUpdaterRegistratorHandler;
import winsome.connection.server_api.wallet_notifier.WalletNotificationUpdater;
import winsome.generic.SerializerWrapper;
import winsome.server_app.internal.threadpool.ServerThreadpool;
import winsome.server_app.internal.threadpool.ServerThreadpoolImpl;

public class WinsomeServerImpl implements WinsomeServer
{
	private final ServerSettings settings;
	private final WinsomeDataImpl winsome_data;
	private final ServerThreadpoolImpl threadpool;
	
	public WinsomeServerImpl(ServerSettings settings, FollowerUpdaterRegistratorHandler follower_updater, WalletNotificationUpdater wallet_updater)
	{
		this.settings = settings;
		threadpool = new ServerThreadpoolImpl();
		winsome_data = getSerializedData(settings.save_file);	
		winsome_data.setUpdaters(follower_updater, wallet_updater);
	}
	
	private WinsomeDataImpl getSerializedData(String filename)
	{
		if(filename == null || !Files.exists(Paths.get(filename)))
		{
			return new WinsomeDataImpl();
		}
		else
		{
			return tryGetSerializedData(filename);
		}
	}
	
	private WinsomeDataImpl tryGetSerializedData(String filename)
	{
		try
		{
			byte[] data = Files.readAllBytes(Paths.get(filename));
			return SerializerWrapper.deserialize(data, WinsomeDataImpl.class);				
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.toString());
		}
	}
	
	public void startServer()
	{
		threadpool.startThreadpool();
	}
	
	public void shutdownServer()
	{
		threadpool.stopThreadpool();
		saveToFile();
	}
	
	@Override
	public WinsomeData getWinsomeData()
	{
		return winsome_data;
	}
	
	@Override
	public ServerSettings getSettings()
	{
		return settings;
	}
	
	@Override
	public ServerThreadpool getThreadpool()
	{
		return threadpool;
	}
	
	public void saveToFile()
	{
		threadpool.pauseThreadpool();		
		WinsomeDataImpl clone = winsome_data.clone();		
		threadpool.resumeThreadpool();
		serializeAndSave(clone);
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
}

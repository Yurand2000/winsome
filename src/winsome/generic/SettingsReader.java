package winsome.generic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSerialize
public class SettingsReader
{
	public SettingsReader() { }
	
	public static void serializeToFile(SettingsReader settings, String filename)
	{
		try
		{
			Path path = Paths.get(filename);
			byte[] data = SerializerWrapper.serialize(settings);
			Files.write(path, data);
		}
		catch (IOException e) { }
	}
	
	protected static <T extends SettingsReader> T deserializeFromFile(Class<T> settings_class, String filename)
	{
		byte[] data = readFile(filename);
		if(data.length > 0)
		{
			return deserializeFromData(settings_class, data);
		}
		else
		{
			return null;
		}
	}
	
	private static <T extends SettingsReader> T deserializeFromData(Class<T> settings_class, byte[] data)
	{
		try
		{
			return SerializerWrapper.deserialize(data, settings_class);
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	private static byte[] readFile(String filename)
	{
		try
		{
			Path path = Paths.get(filename);
			return Files.readAllBytes(path);
		}
		catch (Exception e)
		{
			return new byte[0];
		}
	}
}

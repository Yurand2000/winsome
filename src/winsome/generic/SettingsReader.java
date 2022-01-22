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
			try
			{
				return SerializerWrapper.deserialize(data, settings_class);
			}
			catch (IOException e) { }

			return null;
		}
		else
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

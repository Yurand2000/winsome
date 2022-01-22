package winsome.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SerializerWrapper
{	
	private static ObjectMapper mapper = null;
	
	public static ObjectMapper instance()
	{
		if(mapper == null)
		{
			mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			//mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		}
		return mapper;
	}
	
	public static <T> byte[] serializeCompact(T obj) throws IOException
	{
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
		byte[] data = instance().writeValueAsBytes(obj);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return data;		
	}
	
	public static <T> byte[] serialize(T obj) throws IOException
	{
		return instance().writeValueAsBytes(obj);		
	}
	
	public static <T> T deserialize(byte[] data, Class<T> arrayClass) throws IOException
	{
		return instance().readValue(data, arrayClass);
	}
	
	public static <T> T deserialize(JsonParser data, Class<T> arrayClass) throws IOException
	{
		return instance().readValue(data, arrayClass);
	}
	
	public static <T> T deserialize(TreeNode data, Class<T> arrayClass) throws IOException
	{
		return deserialize(data.traverse(), arrayClass);
	}
	
	public static void addDeserializer(Class<?> type)
	{
		instance().registerSubtypes(type);
	}
	
	public static void addDeserializers(Class<?>... types)
	{
		instance().registerSubtypes(types);
	}
}
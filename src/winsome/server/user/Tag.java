package winsome.server.user;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Tag.TagSerializer.class)
@JsonDeserialize(using = Tag.TagDeserializer.class)
public class Tag
{
	@JsonProperty() public final String tag;
	
	@SuppressWarnings("unused")
	private Tag() { tag = null; }
	
	public Tag(String tag)
	{
		this.tag = tag;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o.getClass() == Tag.class)
		{
			return ((Tag)o).tag.equals(tag);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		return tag;
	}
	
	public static class TagSerializer extends JsonSerializer<Tag>
	{
		@Override
		public void serialize(Tag value, JsonGenerator gen, SerializerProvider serializers) throws IOException
		{
			gen.writeString(value.tag);			
		}
	}
	
	public static class TagDeserializer extends JsonDeserializer<Tag>
	{
		@Override
		public Tag deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
		{
			TreeNode n = p.readValueAsTree();
			if(n.isValueNode())
			{
				String tag = n.toString();
				tag = tag.substring(1, tag.length() - 1);
				return new Tag(tag);
			}
			else
			{
				throw new IOException("Error in tag deserialization.");
			}
		}
		
	}
}

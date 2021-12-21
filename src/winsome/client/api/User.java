package winsome.client.api;

import static java.util.Arrays.copyOf;

public class User
{
	public final String username;
	public final Tag[] tags;
	
	public User(String username, Tag[] tags)
	{
		this.username = username;
		this.tags = copyOf(tags, tags.length);
	}
	
	public static class Tag
	{
		public final String tag;
		
		public Tag(String tag)
		{
			this.tag = tag;
		}
		
		@Override
		public boolean equals(Object o)
		{
			return
				o != null &&
				o.getClass() == this.getClass() &&
				((Tag)o).tag.equals(tag);
		}
	}
}

package winsome.client_app.api;

public class User
{
	public final String username;
	public final Tag[] tags;
	
	public User(String username, Tag[] tags)
	{
		this.username = username;
		this.tags = new Tag[tags.length];
		for(int i = 0; i < tags.length; i++)
		{
			this.tags[i] = new Tag(tags[i]);
		}
	}
	
	public static class Tag
	{
		public final String tag;
		
		public Tag(String tag)
		{
			this.tag = tag;
		}
		
		public Tag(Tag tag)
		{
			this(tag.tag);
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

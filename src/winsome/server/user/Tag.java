package winsome.server.user;

public class Tag
{
	public final String tag;
	
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
}

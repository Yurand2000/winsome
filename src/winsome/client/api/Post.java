package winsome.client.api;

import java.util.Map;
import java.util.TreeMap;
import static java.util.Collections.unmodifiableMap;

public class Post
{
	public final Integer postId;
	public final String author;
	public final String title;
	public final String content;
	public final Map<Integer, Rating> ratings;
	public final Map<Integer, String> comments;
	
	public Post(Integer postId, String author, String title, String content, Map<Integer, Rating> ratings, Map<Integer, String> comments)
	{
		this.postId = postId;
		this.author = author;
		this.title = title;
		this.content = content;
		this.ratings = unmodifiableMap(new TreeMap<Integer, Rating>(ratings));
		this.comments = unmodifiableMap(new TreeMap<Integer, String>(comments));
	}
	
	public static Rating makePositiveRating()
	{
		return new Rating(true);
	}
	
	public static Rating makeNegativeRating()
	{
		return new Rating(false);
	}
	
	public class Rating
	{
		private final boolean positive;
		
		public Rating(boolean positive)
		{
			this.positive = positive;
		}
		
		public boolean isUpVote()
		{
			return positive;
		}
		
		public boolean isDownVote()
		{
			return !positive;
		}
	}
}

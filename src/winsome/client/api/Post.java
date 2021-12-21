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
	public final Integer positive_ratings;
	public final Integer negative_ratings;
	public final Map<Integer, String> comments;
	
	public Post(Integer postId, String author, String title, String content,
			Integer positive_ratings, Integer negative_ratings, Map<Integer, String> comments)
	{
		this.postId = postId;
		this.author = author;
		this.title = title;
		this.content = content;
		this.positive_ratings = positive_ratings;
		this.negative_ratings = negative_ratings;
		this.comments = unmodifiableMap(new TreeMap<Integer, String>(comments));
	}
}

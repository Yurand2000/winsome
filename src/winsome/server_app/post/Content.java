package winsome.server_app.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Content
{
	@JsonProperty() public final String title;
	@JsonProperty() public final String author;
	@JsonProperty() public final String content;
	
	public Content() { title = null; author = null; content = null; }
	
	public Content(String title, String author, String content)
	{
		this.title = title;
		this.author = author;
		this.content = content;
	}
}

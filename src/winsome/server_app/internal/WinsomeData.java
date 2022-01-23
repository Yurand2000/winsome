package winsome.server_app.internal;

import java.util.Map;

import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

public interface WinsomeData
{
	Map<Integer, GenericPost> getPosts();
	Map<String, User> getUsers();
}

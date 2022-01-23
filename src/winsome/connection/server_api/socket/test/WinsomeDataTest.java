package winsome.connection.server_api.socket.test;

import java.util.Map;

import winsome.server_app.internal.WinsomeData;
import winsome.server_app.post.GenericPost;
import winsome.server_app.user.User;

class WinsomeDataTest implements WinsomeData
{

	@Override
	public Map<Integer, GenericPost> getPosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}

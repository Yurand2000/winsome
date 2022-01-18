package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import winsome.generic.SerializerWrapper;
import winsome.server.user.Tag;
import winsome.server.user.User;
import winsome.server.user.UserFactory;

class UserFactoryTest {

	@Test
	void test() throws IOException
	{
		User user = UserFactory.makeNewUser("user", "pass", new Tag[]{new Tag("ciao"), new Tag("sport")});
		user.wallet.addTransaction(50L);
		user.wallet.addTransaction(40L);
		user.addFollower("follower");
		user.addFollowing("following");
		user.addPost(50);
		byte[] data = UserFactory.serializeUser(user);
		System.out.println(new String(data));
		
		user = SerializerWrapper.deserialize(data, User.class);
		data = UserFactory.serializeUser(user);
		System.out.println(new String(data));
	}

}

package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import winsome.server.user.Tag;
import winsome.server.user.User;
import winsome.server.user.UserFactory;

class UserFactoryTest {

	@Test
	void checkMakeNewUser()
	{
		User new_user = UserFactory.makeNewUser("username", "password", new Tag[] {new Tag("sport")});
		assertEquals(new_user.username, "username");
		
		assertTrue(new_user.login.checkPassword("password"));
		
		assertEquals(new_user.tags.size(), 1);
		assertTrue(new_user.tags.get(0).equals(new Tag("sport")));
		
		assertEquals(new_user.wallet.getCurrentTotal(), 0);
		assertTrue(new_user.wallet.getTransactions().isEmpty());
		
		assertEquals(new_user.countFollowers(), 0);
		assertEquals(new_user.countFollowing(), 0);
		assertEquals(new_user.countPosts(), 0);
	}
}

package winsome.server_app.user.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import winsome.server_app.user.Tag;
import winsome.server_app.user.User;
import winsome.server_app.user.UserFactory;

class TEST_UserFactory
{

	@Test
	void checkMakeNewUser()
	{
		User new_user = UserFactory.makeNewUser("username", "password", new Tag[] {new Tag("sport")});
		assertEquals(new_user.username, "username");
		
		assertTrue(new_user.login.checkPassword("password"));
		
		assertEquals(new_user.tags.size(), 1);
		assertTrue(new_user.tags.get(0).equals(new Tag("sport")));
		
		assertEquals(new_user.getWallet().getCurrentTotal(), 0);
		assertTrue(new_user.getWallet().getTransactions().isEmpty());
		
		assertEquals(new_user.countFollowers(), 0);
		assertEquals(new_user.countFollowing(), 0);
		assertEquals(new_user.countPosts(), 0);
	}
}

package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import org.junit.jupiter.api.Test;
import winsome.server.user.LoginInformation;
import winsome.server.user.Tag;
import winsome.server.user.User;
import winsome.server.user.exceptions.FollowerAlreadyAddedException;
import winsome.server.user.exceptions.UserWasNotFollowedException;
import winsome.server.wallet.Wallet;

class UserTest
{
	@Test
	@SuppressWarnings("unused")
	void constructors()
	{
		assertDoesNotThrow( () -> {
			User full_constructor = new User(
				"username", new LoginInformation(0, new byte[]{}),
				new Tag[] { new Tag("") }, new HashSet<String>(),
				new HashSet<String>(), new HashSet<Integer>(), new Wallet());
		} );
		
		assertThrows(RuntimeException.class, () -> {
			User full_constructor = new User(
					"username", new LoginInformation(0, new byte[]{}),
					new Tag[]{}, new HashSet<String>(),
					new HashSet<String>(), new HashSet<Integer>(), new Wallet());
		});
		
		assertThrows(RuntimeException.class, () -> {
			User full_constructor = new User(
					"username", new LoginInformation(0, new byte[]{}),
					new Tag[6], new HashSet<String>(),
					new HashSet<String>(), new HashSet<Integer>(), new Wallet());
		});
		
		assertDoesNotThrow( () -> {
			User partial_constructor = new User(
				"username", new LoginInformation(0, new byte[]{}),
				new Tag[] { new Tag("") }, new Wallet());
		} );
		
		assertThrows(RuntimeException.class, () -> {
			User partial_constructor = new User(
					"username", new LoginInformation(0, new byte[]{}),
					new Tag[]{}, new Wallet());
		});
		
		assertThrows(RuntimeException.class, () -> {
			User partial_constructor = new User(
					"username", new LoginInformation(0, new byte[]{}),
					new Tag[6], new Wallet());
		});
	}
	
	@Test
	void checkAddFollower()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1], new Wallet());
		assertTrue(user.getFollowers().isEmpty());
		
		user.addFollower("ciao");
		assertFalse(user.getFollowers().isEmpty());
		assertTrue(user.getFollowers().contains("ciao"));
	}
	
	@Test
	void checkRemoveFollower()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1], new Wallet());
		user.addFollower("ciao");
		assertTrue(user.getFollowers().contains("ciao"));
		
		user.removeFollower("ciao");
		assertTrue(user.getFollowers().isEmpty());
	}
	
	@Test
	void checkAddFollowing()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1], new Wallet());
		assertTrue(user.getFollowing().isEmpty());
		user.addFollowing("ciao");

		assertFalse(user.getFollowing().isEmpty());		
		assertTrue(user.getFollowing().contains("ciao"));
		
		assertThrows(FollowerAlreadyAddedException.class, () -> {
			user.addFollowing("ciao");
		});
	}
	
	@Test
	void checkRemoveFollowing()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1], new Wallet());
		user.addFollowing("ciao");
		assertTrue(user.getFollowing().contains("ciao"));
		
		user.removeFollowing("ciao");
		assertTrue(user.getFollowing().isEmpty());
		
		assertThrows(UserWasNotFollowedException.class, () -> {
			user.removeFollowing("ciao");
		});
	}
	
	@Test
	void checkAddPost()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1], new Wallet());
		Integer postId = 2310;
		assertTrue(user.getPosts().isEmpty());
		
		user.addPost(postId);
		assertFalse(user.getPosts().isEmpty());
		assertTrue(user.getPosts().contains(postId));
	}
	
	@Test
	void checkRemovePost()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1], new Wallet());
		Integer postId = 2310;
		user.addPost(postId);
		assertTrue(user.getPosts().contains(postId));
		
		user.deletePost(postId);
		assertTrue(user.getPosts().isEmpty());
	}
}

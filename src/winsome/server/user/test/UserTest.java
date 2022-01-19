package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
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
	}
	
	@Test
	void checkAddFollower()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1]);
		assertEquals(user.countFollowers(), 0);
		
		user.addFollower("ciao");
		assertEquals(user.countFollowers(), 1);
		assertTrue(user.isFollowedBy("ciao"));
	}
	
	@Test
	void checkRemoveFollower()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1]);
		user.addFollower("ciao");
		assertTrue(user.isFollowedBy("ciao"));
		
		user.removeFollower("ciao");
		assertEquals(user.countFollowers(), 0);
	}
	
	@Test
	void checkAddFollowing()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1]);
		assertEquals(user.countFollowing(), 0);
		user.addFollowing("ciao");

		assertEquals(user.countFollowing(), 1);
		assertTrue(user.isFollowing("ciao"));
		
		assertThrows(FollowerAlreadyAddedException.class, () -> {
			user.addFollowing("ciao");
		});
	}
	
	@Test
	void checkRemoveFollowing()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1]);
		user.addFollowing("ciao");
		assertTrue(user.isFollowing("ciao"));
		
		user.removeFollowing("ciao");
		assertEquals(user.countFollowing(), 0);
		
		assertThrows(UserWasNotFollowedException.class, () -> {
			user.removeFollowing("ciao");
		});
	}
	
	@Test
	void checkAddPost()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1]);
		Integer postId = 2310;
		assertEquals(user.countPosts(), 0);
		
		user.addPost(postId);
		assertEquals(user.countPosts(), 1);
		assertTrue(user.isAuthorOfPost(postId));
	}
	
	@Test
	void checkRemovePost()
	{
		User user = new User("username", new LoginInformation(0, new byte[]{}), new Tag[1]);
		Integer postId = 2310;
		user.addPost(postId);
		assertTrue(user.isAuthorOfPost(postId));
		
		user.deletePost(postId);
		assertEquals(user.countPosts(), 0);
	}
	
	@Test
	void checkClone()
	{
		User user = new User("username", new LoginInformation("ciao mondo"), new Tag[] { new Tag("ciao") });
		user.addFollower("a");
		user.addFollowing("b");
		user.addPost(90);
		
		User clone = user.clone();
		clone.addFollower("b");
		clone.addFollowing("c");
		clone.addPost(48);
		
		assertEquals(clone.username, user.username);
		assertTrue(clone.login.checkPassword("ciao mondo"));
		assertEquals(clone.tags.get(0).tag, user.tags.get(0).tag);

		assertTrue(clone.isFollowedBy("a"));
		assertTrue(clone.isFollowing("b"));
		assertTrue(clone.isAuthorOfPost(90));
		assertFalse(user.isFollowedBy("b"));
		assertFalse(user.isFollowing("c"));
		assertFalse(user.isAuthorOfPost(48));
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws IOException
	{
		User user = new User("username", new LoginInformation("ciao mondo"), new Tag[] { new Tag("ciao") });
		user.addFollower("a");
		user.addFollowing("b");
		user.addPost(90);
		user.wallet.addTransaction(50L);
		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(user); } );
		
		byte[] data = SerializerWrapper.serialize(user);
		assertDoesNotThrow(() -> { User u = SerializerWrapper.deserialize(data, User.class); } );
		
		User u = SerializerWrapper.deserialize(data, User.class);
		assertEquals(u.username, user.username);
		assertTrue(u.login.checkPassword("ciao mondo"));
		assertEquals(u.tags.get(0).tag, user.tags.get(0).tag);
		assertTrue(u.isFollowedBy("a"));
		assertTrue(u.isFollowing("b"));
		assertTrue(u.isAuthorOfPost(90));
		assertEquals(u.wallet.getCurrentTotal(), user.wallet.getCurrentTotal());
	}
}

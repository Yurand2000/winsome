package winsome.console.commands.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Field;
import winsome.console.CannotExecuteException;
import winsome.console.commands.RegisterExecutor;
import winsome.client.Connection;
import winsome.client.api.User;
import winsome.client.api.exceptions.UsernameAlreadyTakenException;

class TestRegisterCommand
{
	RegisterExecutor command;
	TestClientAPI client_api;

	@BeforeEach
	void setup() throws Exception
	{
		command = new RegisterExecutor(null);
		client_api = new TestClientAPI();
		
		//sets the api via reflex
		Field singletonAPI = Connection.class.getDeclaredField("client_api");
		singletonAPI.setAccessible(true);
		singletonAPI.set(null, client_api);
	}
	
	@Test
	void testConstructor() { }
	
	@Test
	void testCommandWithFiveTags()
	{
		command.executeString("register username password tag1 tag2 tag3 tag4 tag5");
		client_api.checkRegisterArguments( "username", "password", new User.Tag[]
			{ new User.Tag("tag1"), new User.Tag("tag2"), new User.Tag("tag3"),
			  new User.Tag("tag4"), new User.Tag("tag5") } );
	}
	
	@Test
	void testCommandWithFourTags()
	{
		command.executeString("register username password tag1 tag2 tag3 tag4");
		client_api.checkRegisterArguments( "username", "password", new User.Tag[]
			{ new User.Tag("tag1"), new User.Tag("tag2"), new User.Tag("tag3"), new User.Tag("tag4") } );
	}
	
	@Test
	void testCommandWithThreeTags()
	{
		command.executeString("register username password tag1 tag2 tag3");
		client_api.checkRegisterArguments( "username", "password", new User.Tag[]
			{ new User.Tag("tag1"), new User.Tag("tag2"), new User.Tag("tag3") } );
	}
	
	@Test
	void testCommandWithTwoTags()
	{
		command.executeString("register username password tag1 tag2");
		client_api.checkRegisterArguments( "username", "password", new User.Tag[]
			{ new User.Tag("tag1"), new User.Tag("tag2") } );
	}
	
	@Test
	void testCommandWithOneTag()
	{
		command.executeString("register username password tag1");
		client_api.checkRegisterArguments( "username", "password", new User.Tag[]{ new User.Tag("tag1") } );
	}
	
	@Test
	void testCommandWithEmptyUsernameThrows()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("register  password tag1"));
	}
	
	@Test
	void testCommandWithEmptyPasswordThrows()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username  tag1"));
	}
	
	@Test
	void testCommandWithZeroOrSixTagsThrows()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password tag1 tag2 tag3 tag4 tag5 tag6"));
	}
	
	@Test
	void testCommandWithDoubleSpacesThrows()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("register  username password tag1 tag2 tag3 tag4"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username  password tag1 tag2 tag3 tag4"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password  tag1 tag2 tag3 tag4"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password tag1  tag2 tag3 tag4"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password tag1 tag2   tag3 tag4"));
	}
	
	@Test
	void testCommandWithTooShortOrTooLongPasswordThrows()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username this_password_is_too_long_to_be_used tag1"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username pwd tag1"));
	}
	
	@Test
	void testCommandWithAlreadyTakenUsername()
	{
		client_api.setUsernameAlreadyTaken();
		assertThrows(UsernameAlreadyTakenException.class, () -> command.executeString("register username password tag1"));
		client_api.checkRegisterArguments( "username", "password", new User.Tag[]{ new User.Tag("tag1") } );
	}
}

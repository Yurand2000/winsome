package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import winsome.client_app.api.exceptions.UsernameAlreadyTakenException;
import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.RegisterExecutor;

class TEST_RegisterExecutor extends CommandExecutorTest
{
	RegisterExecutor command;

	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		command = new RegisterExecutor(null);
	}
	
	@Test
	void testCommandWithFiveTags()
	{
		command.executeString("register username password tag1 tag2 tag3 tag4 tag5");
		client_api.checkRegisterArguments( "username", "password", new String[]{ "tag1","tag2", "tag3", "tag4", "tag5" } );
	}
	
	@Test
	void testCommandWithFourTags()
	{
		command.executeString("register username password tag1 tag2 tag3 tag4");
		client_api.checkRegisterArguments( "username", "password", new String[]{ "tag1","tag2", "tag3", "tag4" } );
	}
	
	@Test
	void testCommandWithThreeTags()
	{
		command.executeString("register username password tag1 tag2 tag3");
		client_api.checkRegisterArguments( "username", "password", new String[]{ "tag1","tag2", "tag3" } );
	}
	
	@Test
	void testCommandWithTwoTags()
	{
		command.executeString("register username password tag1 tag2");
		client_api.checkRegisterArguments( "username", "password", new String[]{ "tag1","tag2" } );
	}
	
	@Test
	void testCommandWithOneTag()
	{
		command.executeString("register username password tag1");
		client_api.checkRegisterArguments( "username", "password", new String[]{ "tag1" } );
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
		client_api.checkRegisterArguments( "username", "password", new String[]{ "tag1" } );
	}
	
	@Test
	void testMispellFails()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("registe user pass tag"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("registeruser pass tag"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("registeer user pass tag"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("regist user pass tag"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password tag1@#"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username password#$ tag1"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("register username^&# password tag1"));
	}
}

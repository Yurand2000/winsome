package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.client_app.api.exceptions.*;
import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.LoginExecutor;

class TEST_LoginExecutor extends CommandExecutorTest
{
	LoginExecutor command;

	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		command = new LoginExecutor(null);
	}
	
	@Test
	void testLoginCorrect()
	{
		command.executeString("login username password");
		client_api.checkLoginArguments("username", "password");
	}
	
	@Test
	void testLoginNoUsernameFails()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("login  password"));
	}
	
	@Test
	void testLoginNoPasswordFails()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("login username "));
	}
	
	@Test
	void testLoginUnknownUsernameFails()
	{
		client_api.setUnknownUsername();
		assertThrows(UnknownUsernameException.class, () -> command.executeString("login username password"));
	}
	
	@Test
	void testLoginIncorrectPasswordFails()
	{
		client_api.setIncorrectPassword();
		assertThrows(IncorrectPasswordException.class, () -> command.executeString("login username password"));
	}
	
	@Test
	void testLoginAlreadyLoggedInFails()
	{
		client_api.setLoggedIn();
		assertThrows(AlreadyLoggedInException.class, () -> command.executeString("login username password"));
	}
	
	@Test
	void testMispellFails()
	{
		assertThrows(CannotExecuteException.class, () -> command.executeString("loginn username password"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("logi username password"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("logiusername password"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("loginusername password"));
		assertThrows(CannotExecuteException.class, () -> command.executeString("login usernamepassword"));
	}
}

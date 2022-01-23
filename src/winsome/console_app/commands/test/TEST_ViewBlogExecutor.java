package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.ViewBlogExecutor;

class TEST_ViewBlogExecutor extends CommandExecutorTest
{
	ViewBlogExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		
		executor = new ViewBlogExecutor(null);
	}

	@Test
	void test()
	{
		executor.executeString("blog");
		client_api.getLoggedAPI().checkViewBlogExecuted();
	}
	
	@Test
	void testLogoutFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("blogg"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("blo"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("bloog"));
	}
}

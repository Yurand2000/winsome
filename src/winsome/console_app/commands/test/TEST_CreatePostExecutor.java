package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.CannotExecuteException;
import winsome.console_app.commands.CreatePostExecutor;

class TEST_CreatePostExecutor extends CommandExecutorTest
{
	CreatePostExecutor executor;
	
	@BeforeEach
	@Override
	void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		super.setup();
		executor = new CreatePostExecutor(null);
	}

	@Test
	void test()
	{
		client_api.getLoggedAPI().setCreatePostExpected("titolo", "contenuti");
		assertDoesNotThrow(() -> executor.executeString("post <titolo> <contenuti>"));
		client_api.getLoggedAPI().checkCreatePostExecuted();
		
		client_api.getLoggedAPI().setCreatePostExpected("ciao mondo", "contenuti");
		assertDoesNotThrow(() -> executor.executeString("post <ciao mondo> <contenuti>"));
		client_api.getLoggedAPI().checkCreatePostExecuted();
		
		client_api.getLoggedAPI().setCreatePostExpected("ciao mondo", "lorem ipsum");
		assertDoesNotThrow(() -> executor.executeString("post <ciao mondo> <lorem ipsum>"));
		client_api.getLoggedAPI().checkCreatePostExecuted();
	}

	@Test
	void testFailIfTitleTooLong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("post <questo titolo e' troppo lungo!> <contenuti>"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("post <123456789012345678901> <contenuti>")); //21 chars
		
		client_api.getLoggedAPI().setCreatePostExpected("12345678901234567890", "contenuti");
		assertDoesNotThrow(() -> executor.executeString("post <12345678901234567890> <contenuti>")); //20 chars
		client_api.getLoggedAPI().checkCreatePostExecuted();
	}

	@Test
	void testFailIfContentTooLong()
	{
		String content = "";
		for(int i = 0; i < 500; i++)
			content = content + 'a';
		
		final String exactCommand = "post <title> <" + content + ">";
		client_api.getLoggedAPI().setCreatePostExpected("title", content);
		assertDoesNotThrow(() -> executor.executeString(exactCommand)); //500 chars
		client_api.getLoggedAPI().checkCreatePostExecuted();
		
		final String tooLongCommand = "post <title> <" + content + "a>"; // 501 chars
		assertThrows(CannotExecuteException.class, () -> executor.executeString(tooLongCommand));
	}
	
	@Test
	void testFailsIfSpelledWrong()
	{
		assertThrows(CannotExecuteException.class, () -> executor.executeString("post <titolo <contenuti>"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("post <titolo> contenuti"));
		
		assertThrows(CannotExecuteException.class, () -> executor.executeString("postt <a> <b>"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("pos <a> <b>"));
		assertThrows(CannotExecuteException.class, () -> executor.executeString("post <a><b>"));
	}
}

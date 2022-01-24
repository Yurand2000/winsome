package winsome.server_app.internal.tasks.rmi.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.exceptions.IncorrectFormatException;
import winsome.client_app.api.exceptions.TooManyTagsException;
import winsome.client_app.api.exceptions.UsernameAlreadyTakenException;
import winsome.server_app.internal.tasks.rmi.RegisterNewUserTask;

class TEST_RegisterNewUserTask
{
	WinsomeDataTest data;
	ServerThreadpoolTest pool;
	RegisterNewUserTask task;
	
	@BeforeEach
	void setup()
	{
		data = new WinsomeDataTest();
		pool = new ServerThreadpoolTest();
		task = new RegisterNewUserTask(data, "user", "pass", new String[] {"tag1"});
	}

	@Test
	void testNormalOperation() throws InterruptedException
	{
		assertFalse(data.getUsers().containsKey("user"));
		
		task.run(pool);
		task.get();
		
		assertTrue(data.getUsers().containsKey("user"));
	}
	
	@Test
	void testUsernameAlreadyTaken()
	{
		data.getUsers().put("user", null);

		task.run(pool);
		assertThrows(UsernameAlreadyTakenException.class, () -> task.get());
	}

	@Test
	void testCheckArguments()
	{
		task = new RegisterNewUserTask(data, "user$!@", "pass", new String[] {"tag1"});
		task.run(pool);
		assertThrows(IncorrectFormatException.class, () -> task.get());
		
		task = new RegisterNewUserTask(data, "user", "pass^^@", new String[] {"tag1"});
		task.run(pool);
		assertThrows(IncorrectFormatException.class, () -> task.get());
		
		task = new RegisterNewUserTask(data, "user", "pass", new String[] {"tag1#@@!", "tag2"});
		task.run(pool);
		assertThrows(IncorrectFormatException.class, () -> task.get());
		
		task = new RegisterNewUserTask(data, "user", "pass", new String[0]);
		task.run(pool);
		assertThrows(TooManyTagsException.class, () -> task.get());
		
		task = new RegisterNewUserTask(data, "user", "pass", new String[] {"a", "b", "c", "d", "e", "f"});
		task.run(pool);
		assertThrows(TooManyTagsException.class, () -> task.get());
	}
}

package winsome.console_app.commands.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.console_app.commands.HelpExecutor;

class TEST_HelpCommand
{
	HelpExecutor command;

	@BeforeEach
	void setup()
	{
		command = new HelpExecutor(null);
	}
	
	@Test
	void test()
	{
		command.executeString("help");
	}
}

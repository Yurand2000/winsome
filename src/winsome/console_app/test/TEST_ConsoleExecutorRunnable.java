package winsome.console_app.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.console_app.ConsoleExecutorRunnable;

class TEST_ConsoleExecutorRunnable
{
	private PipedInputStream output_reader;
	private PipedOutputStream input_filler;
	private InputStream input;
	private PrintStream output;
	
	private ConsoleExecutorRunnable runnable;
	private ConsoleCommandExecutorTest executor;
	
	@BeforeEach
	void setup() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		input_filler = new PipedOutputStream();
		input = new PipedInputStream(input_filler);

		PipedOutputStream temp = new PipedOutputStream();
		output_reader = new PipedInputStream(temp);
		output = new PrintStream(temp);
		
		runnable = new ConsoleExecutorRunnable();
		
		Field executor_input = ConsoleExecutorRunnable.class.getDeclaredField("input");
		executor_input.setAccessible(true);
		executor_input.set(runnable, input);
		Field executor_output = ConsoleExecutorRunnable.class.getDeclaredField("output");
		executor_output.setAccessible(true);
		executor_output.set(runnable, output);
		
		executor = new ConsoleCommandExecutorTest();
		runnable.setExecutorChain(executor);
	}

	@Test
	void testConsoleRunnable() throws IOException
	{
		input_filler.write(("Input"  + System.lineSeparator()).getBytes());
		executor.setExpectedString("Input");
		
		runnable.run();
		executor.checkExecuted();
		
		assertTrue(output_reader.available() > 0);
		byte[] data = new byte[output_reader.available()];
		output_reader.read(data);
		
		assertEquals(new String(data), "Output" + System.lineSeparator());
	}

}

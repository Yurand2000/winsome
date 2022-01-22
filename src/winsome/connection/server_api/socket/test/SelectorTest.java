package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

public class SelectorTest extends Selector
{
	private boolean wakeup_invoked = false;
	
	public SelectorTest()
	{
		
	}

	@Override
	public boolean isOpen()
	{
		fail();
		return false;
	}

	@Override
	public SelectorProvider provider()
	{
		fail();
		return null;
	}

	@Override
	public Set<SelectionKey> keys()
	{
		fail();
		return null;
	}

	@Override
	public Set<SelectionKey> selectedKeys()
	{
		fail();
		return null;
	}

	@Override
	public int selectNow() throws IOException
	{
		fail();
		return 0;
	}

	@Override
	public int select(long timeout) throws IOException
	{
		fail();
		return 0;
	}

	@Override
	public int select() throws IOException
	{
		fail();
		return 0;
	}

	@Override
	public Selector wakeup()
	{
		wakeup_invoked = true;
		return this;
	}
	
	public void checkWakeupInvoked()
	{
		assertTrue(wakeup_invoked);
		wakeup_invoked = false;
	}

	@Override
	public void close() throws IOException
	{
		
	}

}

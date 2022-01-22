package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

class SelectorTest extends Selector
{
	private boolean wakeup_invoked = false;
	
	public SelectorTest() { }
	public boolean isOpen() { fail(); return false; }
	public SelectorProvider provider() { fail(); return null; }
	public Set<SelectionKey> keys() { fail(); return null; }
	public Set<SelectionKey> selectedKeys() { fail(); return null; }
	public int selectNow() throws IOException { fail(); return 0; }
	public int select(long timeout) throws IOException { fail(); return 0; }
	public int select() throws IOException { fail(); return 0; }
	public void close() throws IOException { fail(); }

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
}

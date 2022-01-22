package winsome.connection.server_api.socket.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

class SelectionKeyTest extends SelectionKey
{
	private boolean cancel_called = false;
	private final SelectorTest selector;
	private int interestOps;
	
	public SelectionKeyTest(SelectorTest selector)
	{
		this.selector = selector;
		this.interestOps = 0;
	}

	public SelectableChannel channel() { fail(); return null; }
	public boolean isValid() { fail(); return false;}

	@Override
	public Selector selector()
	{
		return selector;
	}

	@Override
	public void cancel()
	{
		cancel_called = true;
	}
	
	public void checkCancelCalled()
	{
		assertTrue(cancel_called);
		cancel_called = false;
	}

	@Override
	public int interestOps()
	{
		return interestOps;
	}

	@Override
	public SelectionKey interestOps(int ops)
	{
		interestOps = ops;
		return this;
	}

	@Override
	public int readyOps()
	{
		return SelectionKey.OP_READ;
	}
}

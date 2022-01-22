package winsome.connection.server_api.socket.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class SelectionKeyTest extends SelectionKey
{
	private boolean cancel_called = false;
	private final SelectorTest selector;
	private final SelectableChannel channel;
	private int interestOps;
	
	public SelectionKeyTest(SelectorTest selector, SelectableChannel channel)
	{
		this.selector = selector;
		this.channel = channel;
		this.interestOps = 0;
	}

	@Override
	public SelectableChannel channel()
	{
		return channel;
	}

	@Override
	public Selector selector()
	{
		return selector;
	}

	@Override
	public boolean isValid()
	{
		fail();
		return false;
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

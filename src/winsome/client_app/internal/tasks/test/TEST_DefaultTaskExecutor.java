package winsome.client_app.internal.tasks.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import winsome.client_app.api.APIException;
import winsome.client_app.api.exceptions.ServerInternalException;

class TEST_DefaultTaskExecutor extends TaskExecutorTest
{
	private DefaultTaskExecutorTest task;

	@BeforeEach
	@Override
	void setup()
	{
		super.setup();
		task = new DefaultTaskExecutorTest();
	}
	
	@Test
	void testNormalOperation()
	{
		task.setExecuteArguments(connection, app_api);
		assertDoesNotThrow(() -> task.run(connection, app_api));
		
		task.checkExecuteCalled();
		task.checkOnExceptionNotCalled();
		task.checkOnFinallyCalled();
	}
	
	@Test
	void testThrowsIOException()
	{
		task.throw_io_exception = true;
		
		assertThrows(APIException.class, () -> task.run(connection, app_api));
		
		task.checkExecuteCalled();
		task.checkOnExceptionCalled();
		task.checkOnFinallyCalled();
	}
	
	@Test
	void testThrowsServerInternalException()
	{
		task.throw_server_internal_exception = true;
		
		assertThrows(ServerInternalException.class, () -> task.run(connection, app_api));
		
		task.checkExecuteCalled();
		task.checkOnExceptionCalled();
		task.checkOnFinallyCalled();
	}
	
	@Test
	void testThrowsRequestException()
	{
		task.throw_request_exception_answer = true;
		
		assertThrows(APIException.class, () -> task.run(connection, app_api));
		
		task.checkExecuteCalled();
		task.checkOnExceptionCalled();
		task.checkOnFinallyCalled();
	}
}

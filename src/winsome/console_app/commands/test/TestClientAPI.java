package winsome.console_app.commands.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import winsome.client_app.api.ApplicationAPI;
import winsome.client_app.api.exceptions.*;

class TestClientAPI implements ApplicationAPI
{	
	private TestLoggedClientAPI logged_api = new TestLoggedClientAPI();

	private boolean register_called = false;
	private boolean username_already_taken = false;
	private String register_username;
	private String register_password;
	private String[] register_tags;
	
	@Override
	public void register(String username, String password, String[] tags)
	{
		assertTrue(Pattern.matches("\\w+", username));
		assertTrue(Pattern.matches("\\w+", password));
		assertTrue(password.length() >= 4 && password.length() <= 32);
		for(int i = 0; i < tags.length; i++)
		{
			assertTrue(Pattern.matches("\\w+", tags[i]));
		}
		
		register_username = username;
		register_password = password;
		register_tags = tags.clone();		
		register_called = true;
		
		if(username_already_taken)
		{
			username_already_taken = false;
			throw new UsernameAlreadyTakenException();
		}
	}
	
	public void checkRegisterArguments(String username, String password, String[] tags)
	{
		assertTrue(register_called);
		assertTrue(register_username.equals(username));
		assertTrue(register_password.equals(password));
		assertTrue(register_tags.length == tags.length);
		for(int i = 0; i < tags.length; i++)
		{
			assertTrue(register_tags[i].equals(tags[i]));
		}
	}
	
	public void setUsernameAlreadyTaken()
	{
		username_already_taken = true;
	}
	
	private boolean login_called = false;
	private String login_username;
	private String login_password;

	private boolean unknown_username = false;
	private boolean incorrect_password = false;
	private boolean is_logged_in = false;
	
	@Override
	public void login(String username, String password)
	{
		assertTrue(Pattern.matches("\\w+", username));
		assertTrue(Pattern.matches("\\w+", password));
		
		login_username = username;
		login_password = password;
		login_called = true;
		
		if(is_logged_in)
		{
			throw new AlreadyLoggedInException();
		}
		else if(unknown_username)
		{
			throw new UnknownUsernameException();
		}
		else if(incorrect_password)
		{
			throw new IncorrectPasswordException();
		}
	}
	
	public void checkLoginArguments(String username, String password)
	{
		assertTrue(login_called);
		assertTrue(login_username.equals(username));
		assertTrue(login_password.equals(password));
	}
	
	public void setUnknownUsername()
	{
		unknown_username = true;
	}
	
	public void setIncorrectPassword()
	{
		incorrect_password = true;
	}
	
	public void setLoggedIn()
	{
		is_logged_in = true;
	}
	
	private boolean logout_called = false;

	@Override
	public void logout()
	{
		logout_called = true;
		if(!is_logged_in)
		{
			throw new NotLoggedInException();
		}
	}
	
	public void checkLogoutCalled()
	{
		assertTrue(logout_called);
	}

	@Override
	public TestLoggedClientAPI getLoggedAPI()
	{
		return logged_api;
	}
}

package winsome.server_app.user.test;

import static org.junit.jupiter.api.Assertions.*;

import winsome.server_app.user.PasswordDigester;

public class FakePasswordDigester extends PasswordDigester
{
	private String expected_password = null;
	private long random_number = -1;
	
	public FakePasswordDigester() { }

	@Override
	public byte[] digestPassword(String password, long random_number)
	{
		assertEquals(password, expected_password);
		this.random_number = random_number;
		expected_password = new String();
		return new byte[]{1,3};
	}

	@Override
	public boolean validatePassword(String password, long random_number, byte[] digest)
	{
		assertEquals(password, expected_password);
		assertEquals(random_number, this.random_number);
		assertEquals(digest[0], 1);
		assertEquals(digest[1], 3);
		expected_password = new String();
		random_number = -1;
		return true;
	}
	
	public void setExpectedPassword(String password)
	{
		expected_password = password;
	}
}

package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import winsome.server.user.LoginInformation;
import winsome.server.user.PasswordDigester;

class LoginInformationTest
{
	private FakePasswordDigester test_password_digester;
	
	@BeforeEach
	void setupFakePasswordDigester() throws Exception
	{
		test_password_digester = new FakePasswordDigester();

		//sets the api via reflex
		Field password_digester = PasswordDigester.class.getDeclaredField("me");
		password_digester.setAccessible(true);
		password_digester.set(null, test_password_digester);
	}
	
	@SuppressWarnings("unused")
	@Test
	void constructors() throws Exception
	{
		test_password_digester.setExpectedPassword("password");
		LoginInformation password_constructed = new LoginInformation("password");
		Field login_digest_field = LoginInformation.class.getDeclaredField("password_digest");
		login_digest_field.setAccessible(true);
		byte[] login_digest = (byte[])login_digest_field.get(password_constructed);
		assertTrue(Arrays.equals(login_digest, new byte[] {1,3}));
		
		assertDoesNotThrow(() -> {
			LoginInformation param_constructed = new LoginInformation(458, new byte[] {1,3});
		});
	}

	@Test
	void checkCheckPassword()
	{
		test_password_digester.setExpectedPassword("passwd");
		LoginInformation login = new LoginInformation("passwd");
		
		test_password_digester.setExpectedPassword("passwd");
		login.checkPassword("passwd");
	}
}

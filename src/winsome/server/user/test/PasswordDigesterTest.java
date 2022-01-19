package winsome.server.user.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import winsome.server.user.PasswordDigester;

class PasswordDigesterTest
{
	@Test
	@SuppressWarnings("unused")
	void assertMessageDigestExists()
	{
		assertDoesNotThrow(() -> { PasswordDigester digest = PasswordDigester.instance(); });
	}
	
	@Test
	void checkDigestPassword()
	{
		String password = "ciao mondo";
		long random_number = (long) 51684;
		byte[] digest = PasswordDigester.instance().digestPassword(password, random_number);
		byte[] expected_digest = hexStringToByteArray("5f8bd7f9bed8171b970f0e0718ab5247057e9db03e0c13277207238b70127c31");
		assertTrue(Arrays.equals(digest, expected_digest));
		
		password = "pass mondo";
		random_number = 6846879787868486L;
		digest = PasswordDigester.instance().digestPassword(password, random_number);
		expected_digest = hexStringToByteArray("c41aad3750e54debfd52e15335a66a60035691ef998018b9e716bbc194b1f532");
		assertTrue(Arrays.equals(digest, expected_digest));
		
		digest = PasswordDigester.instance().digestPassword(password, random_number);
		assertTrue(Arrays.equals(digest, expected_digest));
	}
	
	@Test
	void checkValidatePassword()
	{
		String password = "ciao mondo";
		long random_number = (long) 51684;
		byte[] digest = PasswordDigester.instance().digestPassword(password, random_number);		
		assertTrue(PasswordDigester.instance().validatePassword(password, random_number, digest));
		
		password = "pass mondo";
		random_number = 6846879787868486L;
		digest = PasswordDigester.instance().digestPassword(password, random_number);		
		assertTrue(PasswordDigester.instance().validatePassword(password, random_number, digest));
	}
	
	private byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}

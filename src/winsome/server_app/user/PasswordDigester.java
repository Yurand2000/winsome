package winsome.server_app.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class PasswordDigester
{
	private static PasswordDigester me = null;
	private MessageDigest digester = null;
	
	protected PasswordDigester() { };
	
	public static PasswordDigester instance()
	{
		if(me == null)
		{
			me = new PasswordDigester();
			try
			{
				me.digester = MessageDigest.getInstance("SHA-256");
			}
			catch (NoSuchAlgorithmException e)
			{
				throw new RuntimeException(e.getMessage());
			}
		}
		return me;
	}
	
	public byte[] digestPassword(String password, long random_number)
	{
		String new_password = password.concat(String.valueOf(random_number));
		return digester.digest(new_password.getBytes());
	}
	
	public boolean validatePassword(String password, long random_number, byte[] digest)
	{
		return Arrays.equals(digestPassword(password, random_number), digest);
	}
}

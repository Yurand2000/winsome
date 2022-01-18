package winsome.server.user;

import java.io.IOException;
import winsome.generic.SerializerWrapper;
import winsome.server.wallet.Wallet;

public class UserFactory
{	
	public static User makeNewUser(String username, String password, Tag[] tags)
	{
		LoginInformation new_user_login = new LoginInformation(password);
		Wallet new_user_wallet = new Wallet();
		return new User(username, new_user_login, tags, new_user_wallet);
	}
	
	public static User makeUserFromData(byte[] data) throws IOException
	{
		return SerializerWrapper.deserialize(data, User.class);
	}
	
	public static byte[] serializeUser(User user) throws IOException
	{
		return SerializerWrapper.serialize(user);
	}
}

package winsome.server_app.user;

public class UserFactory
{	
	public static User makeNewUser(String username, String password, Tag[] tags)
	{
		LoginInformation new_user_login = new LoginInformation(password);
		return new User(username, new_user_login, tags);
	}
}

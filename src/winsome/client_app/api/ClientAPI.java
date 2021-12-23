package winsome.client_app.api;

public interface ClientAPI
{
	void register(String username, String password, User.Tag[] tags);
	void login(String username, String password);
	void logout();
	
	LoggedClientAPI getLoggedAPI();
}

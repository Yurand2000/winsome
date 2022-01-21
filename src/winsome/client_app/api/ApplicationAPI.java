package winsome.client_app.api;

public interface ApplicationAPI
{
	void register(String username, String password, String[] tags);
	void login(String username, String password);
	void logout();
	
	LoggedClientAPI getLoggedAPI();
}

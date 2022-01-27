package winsome.client_app.api;

/** 
 * The ApplicationAPI interface allows clients to send requests and receive answers to the server application.
 */
public interface ApplicationAPI
{
	/** 
	 * Register a new user in winsome.
	 * @param username the new user username, must be composed of 4 to 32 alphanumeric characters or underscores.
	 * @param password the new user password, must be composed of 4 to 32 alphanumeric characters or underscores. 
	 * @param tags the new user tags, must be 1 to 5 tags, each composed of 3 to 16 lowercase letters only.
	 */
	void register(String username, String password, String[] tags);
	
	/**
	 * Login an existing user to the service.
	 * @param username
	 * @param password
	 */
	void login(String username, String password);
	
	/**
	 * Logout the current logged user to the service.
	 */
	void logout();
	
	/**
	 * Get the API to make requests as a logged user.
	 */
	LoggedClientAPI getLoggedAPI();
}

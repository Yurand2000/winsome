package winsome.server_app.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class LoginInformation
{
	@JsonProperty() private final long random_integer;
	@JsonProperty() private final byte[] password_digest;
	
	@SuppressWarnings("unused")
	private LoginInformation() { random_integer = 0; password_digest = null; }
	
	public LoginInformation(String password)
	{
		random_integer = Instant.now().toEpochMilli();
		password_digest = PasswordDigester.instance().digestPassword(password, random_integer);
	}
	
	public LoginInformation(long random_integer, byte[] digest)
	{
		this.random_integer = random_integer;
		this.password_digest = digest;
	}
	
	public boolean checkPassword(String password)
	{
		return PasswordDigester.instance().validatePassword(password, random_integer, password_digest);
	}
}
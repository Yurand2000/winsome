package winsome.connection.protocols;

import java.nio.charset.StandardCharsets;

public class WalletNotification
{
	private WalletNotification() { }
	
	public static byte[] getNotificationMessage()
	{
		return ("**WALLET UPDATED**").getBytes(StandardCharsets.UTF_8);
	}
}

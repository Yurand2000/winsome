package winsome.connection.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandomGeneratorImpl implements RandomGenerator
{
	private static final String request_string = "/integers/?num=1&min=3500&max=5000&col=1&base=10&format=plain&rnd=new";
	
	public RandomGeneratorImpl() { }

	@Override
	public synchronized Double next()
	{
		try
		{			
			URL request = new URL("https", "www.random.org", request_string);
			HttpURLConnection connection = (HttpURLConnection) request.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			Double read = Double.parseDouble(reader.readLine()) / 100.0;
			
			connection.disconnect();					
			return read;
		}
		catch (IOException e)
		{
			return 1.0;
		}
	}
}

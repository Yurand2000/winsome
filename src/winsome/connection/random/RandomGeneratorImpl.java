package winsome.connection.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandomGeneratorImpl implements RandomGenerator
{
	private final String request_string;
	private final Integer decimals;
	
	public RandomGeneratorImpl()
	{
		request_string = makeRequestString(100.0, 1000.0, 3);
		decimals = 3;
	}
	
	public RandomGeneratorImpl(Double min, Double max, Integer decimals)
	{
		request_string = makeRequestString(min, max, decimals);
		this.decimals = decimals;
	}

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
			
			Double read = Double.parseDouble(reader.readLine()) / Math.pow(10, decimals);
			
			connection.disconnect();					
			return read;
		}
		catch (IOException e)
		{
			return 1.0;
		}
	}

	private String makeRequestString(Double min, Double max, Integer decimals)
	{
		Integer request_min = (int) (min * Math.pow(10, decimals));
		Integer request_max = (int) (max * Math.pow(10, decimals));
		return "/integers/?num=1&min=" + request_min.toString() + "&max=" + request_max.toString() + "&col=1&base=10&format=plain&rnd=new";
	}
}

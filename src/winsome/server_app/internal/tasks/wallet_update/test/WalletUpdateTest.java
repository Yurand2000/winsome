package winsome.server_app.internal.tasks.wallet_update.test;

class WalletUpdateTest
{
	protected WinsomeDataTest data;
	protected ServerThreadpoolTest pool;
	
	void setup()
	{
		pool = new ServerThreadpoolTest();
		data = new WinsomeDataTest();
	}
}

package winsome.console_app;

import java.util.List;

import winsome.client_app.ClientAppAPI;
import winsome.console_app.commands.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class ConsoleAppMain
{
	public static void main(String[] args) throws InterruptedException, UnknownHostException
	{
		ClientAppAPI.startClient( getWalletNotificationAction() );

		ConsoleExecutor.setExecutorChain(generateExecutorChain());
		ConsoleExecutor.startConsoleExecutor();
		printLine("Winsome Console Client Started.");
		
		ConsoleExecutor.joinConsoleExecutor();
		printLine("Winsome Console Client Closed.");
	}
	
	private static Runnable getWalletNotificationAction()
	{
		return () -> {
			printLine("Wallet updated.");
		};
	}
	
	private static void printLine(String line)
	{
		synchronized(System.out)
		{
			System.out.println(line);
		}
	}
	
	private static List<Class<? extends ConsoleCommandExecutor>> generateExecutorChain()
	{
		List<Class<? extends ConsoleCommandExecutor>> chain_executors = new ArrayList<Class<? extends ConsoleCommandExecutor>>();
		chain_executors.add(AddCommentExecutor.class);
		chain_executors.add(CreatePostExecutor.class);
		chain_executors.add(DeletePostExecutor.class);
		chain_executors.add(ExitExecutor.class);
		chain_executors.add(FollowUserExecutor.class);
		chain_executors.add(GetWalletExecutor.class);
		chain_executors.add(GetWalletInBitcoinExecutor.class);
		chain_executors.add(ListFollowersExecutor.class);
		chain_executors.add(ListFollowingExecutor.class);
		chain_executors.add(ListUsersExecutor.class);
		chain_executors.add(LoginExecutor.class);
		chain_executors.add(LogoutExecutor.class);
		chain_executors.add(RatePostExecutor.class);
		chain_executors.add(RegisterExecutor.class);
		chain_executors.add(RewinPostExecutor.class);
		chain_executors.add(ShowFeedExecutor.class);
		chain_executors.add(ShowPostExecutor.class);
		chain_executors.add(UnfollowUserExecutor.class);
		chain_executors.add(ViewBlogExecutor.class);
		
		return chain_executors;
	}
}

package winsome.server.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.Test;

import winsome.server.RewardStateImpl;

class RewardStateImplTest
{
	@Test
	@SuppressWarnings("unused")
	void constructors()
	{
		assertDoesNotThrow(() -> { RewardStateImpl default_constructed = new RewardStateImpl(); });
		
		String[] commentors = new String[] {"Nicola", "Gaio", "Gaio"};
		Map<String, RewardStateImpl.CommentCounter> comment_count = new HashMap<String, RewardStateImpl.CommentCounter>();
		comment_count.put("Gaio", new RewardStateImpl.CommentCounter(3));
		comment_count.put("Lucio", new RewardStateImpl.CommentCounter(1));
		comment_count.put("Nicola", new RewardStateImpl.CommentCounter(7));		
		assertDoesNotThrow(() -> {
			RewardStateImpl param_constructed = new RewardStateImpl(5, 15,
					 new HashSet<String>(), new HashSet<>(Arrays.asList(commentors)), comment_count);
		});
	}
	
	@Test
	void ifTheyLikedThenTheyAreRewarded()
	{
		RewardStateImpl rewardState = new RewardStateImpl();
		rewardState.addLike("A");
		
		assertTrue(rewardState.calcLastReward().contributors.contains("A"));
	}
	
	@Test
	void ifTheyDislikedThenTheyAreNotRewarded()
	{
		RewardStateImpl rewardState = new RewardStateImpl();
		rewardState.addLike("B");
		rewardState.addDislike();
		
		assertFalse(rewardState.calcLastReward().contributors.contains("A"));
	}
	
	@Test
	void ifTheyCommentedThenTheyAreRewarded()
	{
		RewardStateImpl rewardState = new RewardStateImpl();
		rewardState.addComment("A");
		
		assertTrue(rewardState.calcLastReward().contributors.contains("A"));
	}
	
	@Test
	void calculateRewardOnNoInteractions()
	{
		RewardStateImpl rewardState = new RewardStateImpl();
		for(int i = 0; i < 10; i++)
		{
			assertEquals(rewardState.calcLastReward().reward, 0);
		}
	}
	
	@Test
	void calculateRewardOnLikesOnly()
	{
		RewardStateImpl rewardState = new RewardStateImpl(1, 15, new HashSet<String>(), new HashSet<String>(), new HashMap<String, RewardStateImpl.CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 2.77, 0.01);
		
		rewardState = new RewardStateImpl(1, -5, new HashSet<String>(), new HashSet<String>(), new HashMap<String, RewardStateImpl.CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 0, 0.01);
		
		rewardState = new RewardStateImpl(2, 486, new HashSet<String>(), new HashSet<String>(), new HashMap<String, RewardStateImpl.CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 3.09, 0.01);
		
		rewardState = new RewardStateImpl(5, 1001578, new HashSet<String>(), new HashSet<String>(), new HashMap<String, RewardStateImpl.CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 2.76, 0.01);
		
		rewardState = new RewardStateImpl(7, -50157, new HashSet<String>(), new HashSet<String>(), new HashMap<String, RewardStateImpl.CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 0, 0.01);
	}
	
	@Test
	void calculateRewardOnCommentsOnly()
	{
		String[] commentors = new String[] {"Gaio"};
		Map<String, RewardStateImpl.CommentCounter> comment_count = new HashMap<String, RewardStateImpl.CommentCounter>();
		comment_count.put("Gaio", new RewardStateImpl.CommentCounter(1));
		RewardStateImpl rewardState = new RewardStateImpl(1, 0,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 0.69, 0.01);
		
		
		commentors = new String[] {"Gaio", "Lucio", "Nicola", "Lucia"};
		comment_count.clear();
		comment_count.put("Gaio", new RewardStateImpl.CommentCounter(3));
		comment_count.put("Lucio", new RewardStateImpl.CommentCounter(17));
		comment_count.put("Nicola", new RewardStateImpl.CommentCounter(3));
		comment_count.put("Lucia", new RewardStateImpl.CommentCounter(5));
		comment_count.put("Federico", new RewardStateImpl.CommentCounter(2));
		rewardState = new RewardStateImpl(1, 0, new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 2.13, 0.01);
	}
	
	@Test
	void calculateRewardCombined()
	{
		String[] commentors = new String[] {"A", "C", "D"};
		Map<String, RewardStateImpl.CommentCounter> comment_count = new HashMap<String, RewardStateImpl.CommentCounter>();
		comment_count.put("A", new RewardStateImpl.CommentCounter(1));
		comment_count.put("C", new RewardStateImpl.CommentCounter(3));
		comment_count.put("D", new RewardStateImpl.CommentCounter(1));
		RewardStateImpl rewardState = new RewardStateImpl(1, 2,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 2.7, 0.1);
		
		commentors = new String[] {"A", "B", "C", "D", "E"};
		comment_count.clear();
		comment_count.put("A", new RewardStateImpl.CommentCounter(1));
		comment_count.put("B", new RewardStateImpl.CommentCounter(1));
		comment_count.put("C", new RewardStateImpl.CommentCounter(3));
		comment_count.put("D", new RewardStateImpl.CommentCounter(5));
		comment_count.put("E", new RewardStateImpl.CommentCounter(2));
		rewardState = new RewardStateImpl(1, 5,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 3.9, 0.1);
		
		commentors = new String[] {"A", "E"};
		comment_count.clear();
		comment_count.put("A", new RewardStateImpl.CommentCounter(1));
		comment_count.put("E", new RewardStateImpl.CommentCounter(1));
		rewardState = new RewardStateImpl(1, -3,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 1.1, 0.1);
		
		commentors = new String[] {"C"};
		comment_count.clear();
		comment_count.put("C", new RewardStateImpl.CommentCounter(33));
		rewardState = new RewardStateImpl(1, 0,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 1.1, 0.1);
	}
	
	@Test
	void calculateSameRewardLoadedManuallyOrWithMethods()
	{
		String[] commentors = new String[] {"A", "C", "D"};
		Map<String, RewardStateImpl.CommentCounter> comment_count = new HashMap<String, RewardStateImpl.CommentCounter>();
		comment_count.put("A", new RewardStateImpl.CommentCounter(1));
		comment_count.put("C", new RewardStateImpl.CommentCounter(3));
		comment_count.put("D", new RewardStateImpl.CommentCounter(1));
		RewardStateImpl rewardStateManual = new RewardStateImpl(1, 2,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);

		RewardStateImpl rewardStateMethodic = new RewardStateImpl();
		rewardStateMethodic.addComment("A"); rewardStateMethodic.addComment("C"); rewardStateMethodic.addComment("C"); rewardStateMethodic.addComment("C"); rewardStateMethodic.addComment("D");
		rewardStateMethodic.addLike("A"); rewardStateMethodic.addLike("B"); rewardStateMethodic.addLike("C"); rewardStateMethodic.addDislike();
		assertEquals(rewardStateManual.calcLastReward().reward, rewardStateMethodic.calcLastReward().reward, 0.1);
	}
	
	@Test
	void calculateSameRewardOnDifferentIterations()
	{
		RewardStateImpl rewardState = new RewardStateImpl();
		
		rewardState.addLike("A"); rewardState.addDislike(); rewardState.addLike("B"); rewardState.addDislike(); rewardState.addLike("C");
		rewardState.addComment("A"); rewardState.addComment("B");
		assertEquals(rewardState.calcLastReward().reward, 1.79, 0.01);

		rewardState.addLike("A"); rewardState.addDislike(); rewardState.addLike("B"); rewardState.addDislike(); rewardState.addLike("C");
		rewardState.addComment("C"); rewardState.addComment("D");
		assertEquals(rewardState.calcLastReward().reward, 0.89, 0.01);

		rewardState.addLike("A"); rewardState.addDislike(); rewardState.addLike("B"); rewardState.addDislike(); rewardState.addLike("C");
		rewardState.addComment("E"); rewardState.addComment("F");
		assertEquals(rewardState.calcLastReward().reward, 0.59, 0.01);
		
		rewardState.addLike("A"); rewardState.addDislike(); rewardState.addLike("B"); rewardState.addDislike(); rewardState.addLike("C");
		rewardState.addComment("G"); rewardState.addComment("H");
		assertEquals(rewardState.calcLastReward().reward, 0.44, 0.01);
	}
}

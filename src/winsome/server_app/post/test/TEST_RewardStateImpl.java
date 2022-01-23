package winsome.server_app.post.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.Test;

import winsome.generic.SerializerWrapper;
import winsome.server_app.post.RewardStateImpl;
import winsome.server_app.post.RewardState.Reward;
import winsome.server_app.post.RewardStateImpl.CommentCounter;

class TEST_RewardStateImpl
{
	@Test
	@SuppressWarnings("unused")
	void constructors()
	{
		assertDoesNotThrow(() -> { RewardStateImpl default_constructed = new RewardStateImpl(); });
		
		String[] commentors = new String[] {"Nicola", "Gaio", "Gaio"};
		Map<String, CommentCounter> comment_count = new HashMap<String, CommentCounter>();
		comment_count.put("Gaio", new CommentCounter(3));
		comment_count.put("Lucio", new CommentCounter(1));
		comment_count.put("Nicola", new CommentCounter(7));		
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
		RewardStateImpl rewardState = new RewardStateImpl(1, 15, new HashSet<String>(), new HashSet<String>(), new HashMap<String, CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 2.77, 0.01);
		
		rewardState = new RewardStateImpl(1, -5, new HashSet<String>(), new HashSet<String>(), new HashMap<String, CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 0, 0.01);
		
		rewardState = new RewardStateImpl(2, 486, new HashSet<String>(), new HashSet<String>(), new HashMap<String, CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 3.09, 0.01);
		
		rewardState = new RewardStateImpl(5, 1001578, new HashSet<String>(), new HashSet<String>(), new HashMap<String, CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 2.76, 0.01);
		
		rewardState = new RewardStateImpl(7, -50157, new HashSet<String>(), new HashSet<String>(), new HashMap<String, CommentCounter>());
		assertEquals(rewardState.calcLastReward().reward, 0, 0.01);
	}
	
	@Test
	void calculateRewardOnCommentsOnly()
	{
		String[] commentors = new String[] {"Gaio"};
		Map<String, CommentCounter> comment_count = new HashMap<String, CommentCounter>();
		comment_count.put("Gaio", new CommentCounter(1));
		RewardStateImpl rewardState = new RewardStateImpl(1, 0,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 0.69, 0.01);
		
		
		commentors = new String[] {"Gaio", "Lucio", "Nicola", "Lucia"};
		comment_count.clear();
		comment_count.put("Gaio", new CommentCounter(3));
		comment_count.put("Lucio", new CommentCounter(17));
		comment_count.put("Nicola", new CommentCounter(3));
		comment_count.put("Lucia", new CommentCounter(5));
		comment_count.put("Federico", new CommentCounter(2));
		rewardState = new RewardStateImpl(1, 0, new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 2.13, 0.01);
	}
	
	@Test
	void calculateRewardCombined()
	{
		String[] commentors = new String[] {"A", "C", "D"};
		Map<String, CommentCounter> comment_count = new HashMap<String, CommentCounter>();
		comment_count.put("A", new CommentCounter(1));
		comment_count.put("C", new CommentCounter(3));
		comment_count.put("D", new CommentCounter(1));
		RewardStateImpl rewardState = new RewardStateImpl(1, 2,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 2.7, 0.1);
		
		commentors = new String[] {"A", "B", "C", "D", "E"};
		comment_count.clear();
		comment_count.put("A", new CommentCounter(1));
		comment_count.put("B", new CommentCounter(1));
		comment_count.put("C", new CommentCounter(3));
		comment_count.put("D", new CommentCounter(5));
		comment_count.put("E", new CommentCounter(2));
		rewardState = new RewardStateImpl(1, 5,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 3.9, 0.1);
		
		commentors = new String[] {"A", "E"};
		comment_count.clear();
		comment_count.put("A", new CommentCounter(1));
		comment_count.put("E", new CommentCounter(1));
		rewardState = new RewardStateImpl(1, -3,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 1.1, 0.1);
		
		commentors = new String[] {"C"};
		comment_count.clear();
		comment_count.put("C", new CommentCounter(33));
		rewardState = new RewardStateImpl(1, 0,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		assertEquals(rewardState.calcLastReward().reward, 1.1, 0.1);
	}
	
	@Test
	void calculateSameRewardLoadedManuallyOrWithMethods()
	{
		String[] commentors = new String[] {"A", "C", "D"};
		Map<String, CommentCounter> comment_count = new HashMap<String, CommentCounter>();
		comment_count.put("A", new CommentCounter(1));
		comment_count.put("C", new CommentCounter(3));
		comment_count.put("D", new CommentCounter(1));
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
	
	@Test
	void checkClone()
	{
		String[] commentors = new String[] {"A", "C", "D"};
		String[] likers = new String[] {"B", "E"};
		Map<String, CommentCounter> comment_count = new HashMap<String, CommentCounter>();
		comment_count.put("A", new CommentCounter(1));
		comment_count.put("C", new CommentCounter(3));
		comment_count.put("D", new CommentCounter(1));
		RewardStateImpl rs = new RewardStateImpl(1, 2,  new HashSet<String>(Arrays.asList(likers)), new HashSet<String>(Arrays.asList(commentors)), comment_count);
		
		RewardStateImpl clone = rs.clone();
		clone.addComment("Benito");
		clone.addLike("A");
		assertNotEquals(clone.calcLastReward().reward, rs.calcLastReward().reward, 0.001);
	}
	
	@Test
	@SuppressWarnings("unused")
	void checkSerialization() throws IOException
	{
		String[] commentors = new String[] {"A", "C", "D"};
		Map<String, CommentCounter> comment_count = new HashMap<String, CommentCounter>();
		comment_count.put("A", new CommentCounter(1));
		comment_count.put("C", new CommentCounter(3));
		comment_count.put("D", new CommentCounter(1));
		RewardStateImpl rewardState = new RewardStateImpl(1, 10,  new HashSet<String>(), new HashSet<String>(Arrays.asList(commentors)), comment_count);

		assertDoesNotThrow(() -> { byte[] data = SerializerWrapper.serialize(rewardState); } );
		
		byte[] data = SerializerWrapper.serialize(rewardState);
		assertDoesNotThrow(() -> { RewardStateImpl rs = SerializerWrapper.deserialize(data, RewardStateImpl.class); } );
		
		RewardStateImpl rs = SerializerWrapper.deserialize(data, RewardStateImpl.class);
		Reward rs_reward = rs.calcLastReward();
		Reward rewardState_reward = rewardState.calcLastReward();
		assertEquals(rs_reward.reward, rewardState_reward.reward, 0.00001);
		assertTrue(rs_reward.contributors.contains("A"));
		assertTrue(rs_reward.contributors.contains("C"));
		assertTrue(rs_reward.contributors.contains("D"));
	}
}
